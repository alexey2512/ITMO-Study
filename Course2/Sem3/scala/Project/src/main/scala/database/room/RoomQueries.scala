package database.room

import doobie._
import doobie.implicits._
import models.dbmodels._
import models.Aliases._

object RoomQueries {

  def registerRoomQuery(
    name: String,
    level: Int
  ): ConnectionIO[Option[RoomRegistrationInfo]] =
    sql"""
         INSERT INTO rooms (name, level)
         VALUES ($name, $level)
         RETURNING id;
       """.query[RoomRegistrationInfo].option

  def findRoomQuery(id: RoomId): ConnectionIO[Option[RoomInfo]] =
    sql"""
         SELECT name, level
         FROM rooms
         WHERE id = $id;
       """.query[RoomInfo].option

  def deleteRoomQuery(id: RoomId): ConnectionIO[Option[RoomInfo]] =
    sql"""
         DELETE FROM rooms
         WHERE id = $id
         RETURNING name, level;
       """.query[RoomInfo].option

}
