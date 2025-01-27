package database.access

import cats.effect.{IO, Resource}
import doobie.hikari.HikariTransactor
import doobie.implicits._
import AccessQueries._
import error.DBError
import error.DBError._
import models.Aliases._

final class AccessDaoIO(val transactor: Resource[IO, HikariTransactor[IO]]) extends AccessDao[IO] {

  override def postAccessLog(
    employeeId: EmployeeId,
    roomId: RoomId,
    direction: Direction,
    allowed: Boolean
  ): IO[Either[DBError, Int]] =
    for {
      response <- transactor.use(xa =>
        postAccessLogQuery(employeeId, roomId, direction, allowed)
          .transact(xa)
          .attempt
      )
      result = response match {
        case Left(_)  => Left(PostDataError)
        case Right(x) => if (x <= 0) Left(PostDataError) else Right(x)
      }
    } yield result

  override def lastAccess(
    employeeId: EmployeeId,
    roomId: RoomId
  ): IO[Either[DBError, Direction]] =
    for {
      response <- transactor.use(xa => lastAccessQuery(employeeId, roomId).transact(xa).attempt)
      result = response match {
        case Left(_) => Left(ConnectionError)
        case Right(None) =>
          Right("Out") // lets suppose that this is a database functionality
        case Right(Some(dir)) => Right(dir)
      }
    } yield result

}
