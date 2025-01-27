package models.dbmodels

import doobie.Read

import models.Aliases._

case class EmployeeRegistrationInfo(id: EmployeeId, token: Token)

object EmployeeRegistrationInfo {

  implicit val read: Read[EmployeeRegistrationInfo] =
    Read[(EmployeeId, Token)].map { case (id, token) =>
      EmployeeRegistrationInfo(id, token)
    }

}
