package models.dbmodels

import doobie.Read

final case class RoomInfo(name: String, level: Int)

object RoomInfo {

  implicit val read: Read[RoomInfo] =
    Read[(String, Int)].map { case (name, level) =>
      RoomInfo(name, level)
    }

}
