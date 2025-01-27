package logic.employee

import database.employee.EmployeeDao
import cats.effect.IO
import error._
import error.DBError._
import error.ApiError._
import models.apimodels._
import models.dbmodels._
import TokenGenerator._
import models.Aliases._

final class EmployeeLogicIO(val dao: EmployeeDao[IO]) extends EmployeeLogic[IO] {

  override def registerEmployeeLogic: EmployeeRegistrationRequest => IO[
    Either[ApiError, EmployeeRegistrationResponse]
  ] =
    req => {
      val validation: Either[ApiError, String] =
        if (req.name.trim.nonEmpty) Right(req.name)
        else Left(EmptyFieldError("employee name must be non empty"))
      validation match {
        case Left(error) => IO.pure(Left(error))
        case Right(name) =>
          for {
            token    <- generateToken(name)
            response <- dao.registerEmployee(name, req.level, token)
            result = response match {
              case Left(_) => Left(InternalError("some problems occurred"))
              case Right(info) =>
                Right(EmployeeRegistrationResponse(info.id, info.token))
            }
          } yield result
      }
    }

  private def employeeLogic(
    fu: Token => IO[Either[DBError, EmployeeInfo]]
  ): Token => IO[Either[ApiError, EmployeeResponse]] =
    token =>
      for {
        response <- fu(token)
        result = response match {
          case Left(ConnectionError) =>
            Left(InternalError("some problems occurred"))
          case Left(_) =>
            Left(EmployeeNotFound("no such employee with given token"))
          case Right(info) =>
            Right(EmployeeResponse(info.id, info.name, info.level))
        }
      } yield result

  override def advanceEmployeeLogic: Token => IO[Either[ApiError, EmployeeResponse]] = employeeLogic(dao.advanceEmployee)

  override def findEmployeeLogic: Token => IO[Either[ApiError, EmployeeResponse]] = employeeLogic(dao.findEmployee)

  override def delEmployeeLogic: Token => IO[Either[ApiError, EmployeeResponse]] = employeeLogic(dao.deleteEmployee)

}
