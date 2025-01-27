package logic.access

import cats.data.EitherT
import database.employee.EmployeeDao
import database.room.RoomDao
import database.access.AccessDao
import cats.effect.IO
import error._
import error.DBError._
import error.ApiError._
import models.apimodels._
import models.dbmodels._
import models.Aliases._

final class AccessLogicIO(
  val employeeDAO: EmployeeDao[IO],
  val roomDAO: RoomDao[IO],
  val accessDAO: AccessDao[IO]
) extends AccessLogic[IO] {

  private def lastAccess(employeeId: EmployeeId, roomId: RoomId): EitherT[IO, ApiError, Direction] =
    EitherT(accessDAO.lastAccess(employeeId, roomId)).leftMap(_ => InternalError("some problems occurred"): ApiError)

  private def logAttempt(
    emp: EmployeeInfo,
    roomId: RoomId,
    dir: Direction,
    allowed: Boolean
  ): EitherT[IO, ApiError, AccessResponse] =
    EitherT(accessDAO.postAccessLog(emp.id, roomId, dir, allowed))
      .leftMap {
        case PostDataError => InvalidRequestError("failed to log attempt"): ApiError
        case _             => InternalError("some problems occurred"): ApiError
      }
      .map(_ => AccessResponse(allowed, EmployeeResponse(emp.id, emp.name, emp.level)))

  override def accessLogic: ((RoomId, Direction, Token)) => IO[Either[ApiError, AccessResponse]] = { case (roomId, direction, token) =>
    val validation: Either[ApiError, Direction] =
      if (direction == "In" || direction == "Out") Right(direction)
      else Left(InvalidRequestError("direction must be in or out"))
    EitherT
      .fromEither[IO](validation)
      .flatMap { dir =>
        for {
          emp <- EitherT(employeeDAO.findEmployee(token)).leftMap {
            case GetDataError => EmployeeNotFound("no such employee with given token"): ApiError
            case _            => InternalError("some problems occurred"): ApiError
          }
          room <- EitherT(roomDAO.findRoom(roomId)).leftMap {
            case GetDataError => RoomNotFound("no such room with given id"): ApiError
            case _            => InternalError("some problems occurred"): ApiError
          }
          last     <- lastAccess(emp.id, roomId)
          response <- logAttempt(emp, roomId, dir, allowed = last != dir && room.level <= emp.level)
        } yield response
      }
      .value
  }

}
