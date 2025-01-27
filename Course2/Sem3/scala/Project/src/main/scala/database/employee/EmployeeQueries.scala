package database.employee

import doobie._
import doobie.implicits._
import models.dbmodels._
import models.Aliases._

private[database] object EmployeeQueries {

  def registerEmployeeQuery(
    name: String,
    level: Int,
    token: Token
  ): ConnectionIO[Option[EmployeeRegistrationInfo]] =
    sql"""
         INSERT INTO employees (name, level, access_token)
         VALUES ($name, $level, $token)
         RETURNING id,  access_token;
       """.query[EmployeeRegistrationInfo].option

  def advanceEmployeeQuery(token: Token): ConnectionIO[Option[EmployeeInfo]] =
    sql"""
         UPDATE employees
         SET level = level + 1
         WHERE access_token = $token
         RETURNING id, name, level;
       """.query[EmployeeInfo].option

  def findEmployeeQuery(token: Token): ConnectionIO[Option[EmployeeInfo]] =
    sql"""
         SELECT id, name, level
         FROM employees
         WHERE access_token = $token;
       """.query[EmployeeInfo].option

  def deleteEmployeeQuery(token: Token): ConnectionIO[Option[EmployeeInfo]] =
    sql"""
         DELETE FROM employees
         WHERE access_token = $token
         RETURNING id, name, level;
       """.query[EmployeeInfo].option

}
