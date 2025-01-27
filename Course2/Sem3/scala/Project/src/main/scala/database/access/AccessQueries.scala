package database.access

import doobie._
import doobie.implicits._
import models.Aliases._

private[database] object AccessQueries {

  def postAccessLogQuery(
    employeeId: EmployeeId,
    roomId: RoomId,
    direction: Direction,
    allowed: Boolean
  ): ConnectionIO[Int] =
    sql"""
         INSERT INTO access_logs (employee_id, room_id, type, allowed)
         VALUES ($employeeId, $roomId, $direction, $allowed);
       """.update.run

  def lastAccessQuery(
    employeeId: EmployeeId,
    roomId: RoomId
  ): ConnectionIO[Option[Direction]] =
    sql"""
         SELECT type
         FROM access_logs
         WHERE employee_id = $employeeId AND room_id = $roomId AND allowed = TRUE
         ORDER BY date_time DESC LIMIT 1;
       """.query[Direction].option

}
