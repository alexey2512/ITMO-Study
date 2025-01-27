package logic.room

import database.room.RoomDao
import cats.effect.IO
import error._
import error.DBError._
import error.ApiError._
import models.apimodels._
import models.Aliases._

final class RoomLogicIO(val dao: RoomDao[IO]) extends RoomLogic[IO] {

  override def registerRoomLogic: RoomRegistrationRequest => IO[
    Either[ApiError, RoomRegistrationResponse]
  ] =
    req => {
      val validation: Either[ApiError, String] =
        if (req.name.trim.nonEmpty) Right(req.name)
        else Left(EmptyFieldError("room name must be non empty"))
      validation match {
        case Left(error) => IO.pure(Left(error))
        case Right(name) =>
          for {
            response <- dao.registerRoom(name, req.level)
            result = response match {
              case Left(_)     => Left(InternalError("some problems occurred"))
              case Right(info) => Right(RoomRegistrationResponse(info.id))
            }
          } yield result
      }
    }

  override def delRoomLogic: RoomId => IO[Either[ApiError, RoomResponse]] =
    id =>
      for {
        response <- dao.deleteRoom(id)
        result = response match {
          case Left(DeleteDataError) =>
            Left(RoomNotFound("no such room with given id"))
          case Left(_)     => Left(InternalError("some problems occurred"))
          case Right(info) => Right(RoomResponse(info.name, info.level))
        }
      } yield result

}
