package logic.employee

import error._
import models.apimodels._
import models.Aliases._

trait EmployeeLogic[F[_]] {

  def registerEmployeeLogic: EmployeeRegistrationRequest => F[Either[ApiError, EmployeeRegistrationResponse]]
  def findEmployeeLogic: Token => F[Either[ApiError, EmployeeResponse]]
  def advanceEmployeeLogic: Token => F[Either[ApiError, EmployeeResponse]]
  def delEmployeeLogic: Token => F[Either[ApiError, EmployeeResponse]]

}
