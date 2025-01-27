package database.employee

import cats.effect.unsafe.implicits.global
import cats.effect.IO
import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec
import database.Transactor.transactor
import logic.employee.TokenGenerator.generateToken
import models.Aliases._
import error.DBError._

class EmployeeDaoIOSpec extends AnyFlatSpec with Matchers {

  val employeeDao: EmployeeDao[IO] = new EmployeeDaoIO(transactor)

  val name: String           = "EmployeeDaoIOSpec"
  val level: Int             = 2
  val token: Token           = generateToken(name).unsafeRunSync()
  var employeeId: EmployeeId = 0

  "registerEmployee" should "register new employee correctly" in {
    employeeId = employeeDao.registerEmployee(name, level, token).unsafeRunSync() match {
      case Left(e)     => fail(s"unexpected error $e")
      case Right(info) => info.id
    }
  }

  "findEmployee" should "return info about created employee with given token" in {
    employeeDao.findEmployee(token).unsafeRunSync() match {
      case Left(e) => fail(s"unexpected error $e")
      case Right(info) =>
        info.id shouldEqual employeeId
        info.name shouldEqual name
        info.level shouldEqual level
    }
  }

  it should "return error if employee not found" in {
    employeeDao.findEmployee("not existing token").unsafeRunSync() match {
      case Left(e)  => e shouldEqual GetDataError
      case Right(_) => fail("expected some error")
    }
  }

  "advanceEmployee" should "increase employee level" in {
    employeeDao.advanceEmployee(token).unsafeRunSync() match {
      case Left(e) => fail(s"unexpected error $e")
      case Right(info) =>
        info.level shouldEqual level + 1
    }
  }

  "deleteEmployee" should "delete employee from db" in {
    employeeDao.deleteEmployee(token).unsafeRunSync() match {
      case Left(e) => fail(s"unexpected error $e")
      case Right(info) =>
        info.id shouldEqual employeeId
        info.name shouldEqual name
        info.level shouldEqual level + 1
    }
  }

  it should "return an error when repeated query" in {
    employeeDao.deleteEmployee(token).unsafeRunSync() match {
      case Left(e)  => e shouldEqual DeleteDataError
      case Right(_) => fail("expected some error")
    }
  }

}
