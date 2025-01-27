package database.employee

import error.DBError
import models.Aliases.Token
import models.dbmodels._

trait EmployeeDao[F[_]] {

  def registerEmployee(
    name: String,
    level: Int,
    token: Token
  ): F[Either[DBError, EmployeeRegistrationInfo]]

  def advanceEmployee(token: Token): F[Either[DBError, EmployeeInfo]]
  def findEmployee(token: Token): F[Either[DBError, EmployeeInfo]]
  def deleteEmployee(token: Token): F[Either[DBError, EmployeeInfo]]

}
