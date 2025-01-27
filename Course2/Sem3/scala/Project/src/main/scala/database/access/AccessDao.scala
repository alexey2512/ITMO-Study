package database.access

import error.DBError
import models.Aliases._

trait AccessDao[F[_]] {

  def postAccessLog(
    employeeId: EmployeeId,
    roomId: RoomId,
    direction: Direction,
    allowed: Boolean
  ): F[Either[DBError, Int]]

  def lastAccess(
    employeeId: EmployeeId,
    roomId: RoomId
  ): F[Either[DBError, Direction]]

}
