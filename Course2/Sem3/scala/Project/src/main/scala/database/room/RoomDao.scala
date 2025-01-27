package database.room

import models.dbmodels._
import error.DBError
import models.Aliases._

trait RoomDao[F[_]] {

  def registerRoom(
    name: String,
    level: Int
  ): F[Either[DBError, RoomRegistrationInfo]]

  def findRoom(id: RoomId): F[Either[DBError, RoomInfo]]
  def deleteRoom(id: RoomId): F[Either[DBError, RoomInfo]]

}
