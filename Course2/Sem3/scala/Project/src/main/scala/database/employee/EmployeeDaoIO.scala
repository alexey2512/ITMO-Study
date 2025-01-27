package database.employee

import cats.effect.{IO, Resource}
import doobie.hikari.HikariTransactor
import doobie.implicits._
import EmployeeQueries._
import models.dbmodels._
import models.Aliases._
import error.DBError
import error.DBError._
import database.DaoPrivateCommons.specifyWith

final class EmployeeDaoIO(val transactor: Resource[IO, HikariTransactor[IO]]) extends EmployeeDao[IO] {

  override def registerEmployee(
    name: String,
    level: Int,
    token: Token
  ): IO[Either[DBError, EmployeeRegistrationInfo]] =
    for {
      response <- transactor.use(xa => registerEmployeeQuery(name, level, token).transact(xa).attempt)
      result = specifyWith(response, PostDataError)
    } yield result

  override def advanceEmployee(token: Token): IO[Either[DBError, EmployeeInfo]] =
    for {
      response <- transactor.use(xa => advanceEmployeeQuery(token).transact(xa).attempt)
      result = specifyWith(response, UpdateDataError)
    } yield result

  override def findEmployee(token: Token): IO[Either[DBError, EmployeeInfo]] =
    for {
      response <- transactor.use(xa => findEmployeeQuery(token).transact(xa).attempt)
      result = specifyWith(response, GetDataError)
    } yield result

  override def deleteEmployee(token: Token): IO[Either[DBError, EmployeeInfo]] =
    for {
      response <- transactor.use(xa => deleteEmployeeQuery(token).transact(xa).attempt)
      result = specifyWith(response, DeleteDataError)
    } yield result

}
