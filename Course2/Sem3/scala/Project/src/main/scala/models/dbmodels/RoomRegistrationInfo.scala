package models.dbmodels

import doobie.Read
import models.Aliases._

final case class RoomRegistrationInfo(id: RoomId)

object RoomRegistrationInfo {

  implicit val read: Read[RoomRegistrationInfo] =
    Read[Int].map(id => RoomRegistrationInfo(id))

}
