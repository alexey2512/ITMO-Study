package models.dbmodels

import doobie.Read
import models.Aliases._

final case class EmployeeInfo(id: EmployeeId, name: String, level: Int)

object EmployeeInfo {

  implicit val read: Read[EmployeeInfo] =
    Read[(EmployeeId, String, Int)].map { case (id, token, level) =>
      EmployeeInfo(id, token, level)
    }

}
