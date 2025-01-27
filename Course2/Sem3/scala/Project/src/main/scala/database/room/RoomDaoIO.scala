package database.room

import cats.effect.{IO, Resource}
import doobie.hikari.HikariTransactor
import doobie.implicits._
import RoomQueries._
import models.dbmodels._
import error.DBError
import error.DBError._
import database.DaoPrivateCommons.specifyWith
import models.Aliases._

final class RoomDaoIO(val transactor: Resource[IO, HikariTransactor[IO]]) extends RoomDao[IO] {

  override def registerRoom(
    name: String,
    level: Int
  ): IO[Either[DBError, RoomRegistrationInfo]] =
    for {
      response <- transactor.use(xa => registerRoomQuery(name, level).transact(xa).attempt)
      result = specifyWith(response, PostDataError)
    } yield result

  override def findRoom(id: RoomId): IO[Either[DBError, RoomInfo]] =
    for {
      response <- transactor.use(xa => findRoomQuery(id).transact(xa).attempt)
      result = specifyWith(response, GetDataError)
    } yield result

  override def deleteRoom(id: RoomId): IO[Either[DBError, RoomInfo]] =
    for {
      response <- transactor.use(xa => deleteRoomQuery(id).transact(xa).attempt)
      result = specifyWith(response, DeleteDataError)
    } yield result

}
