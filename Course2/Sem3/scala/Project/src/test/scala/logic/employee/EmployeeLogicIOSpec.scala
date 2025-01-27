package logic.employee

import cats.effect.unsafe.implicits.global
import cats.effect.IO
import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec
import database.Transactor.transactor
import database.employee._
import error.ApiError.EmptyFieldError
import models.Aliases._
import models.apimodels._
import error.ApiError._

class EmployeeLogicIOSpec extends AnyFlatSpec with Matchers {

  val employeeDao: EmployeeDao[IO]     = new EmployeeDaoIO(transactor)
  val employeeLogic: EmployeeLogic[IO] = new EmployeeLogicIO(employeeDao)

  val name: String = "EmployeeLogicIOSpec"
  val level: Int   = 2
  var token: Token = ""

  "registerEmployeeLogic" should "register new employee successfully and return generated token" in {
    token = employeeLogic
      .registerEmployeeLogic(EmployeeRegistrationRequest(name, level))
      .unsafeRunSync() match {
      case Left(e)    => fail(s"unexpected error $e")
      case Right(res) => res.token
    }
  }

  it should "reject registration if name is empty" in {
    employeeLogic
      .registerEmployeeLogic(EmployeeRegistrationRequest("  ", 1))
      .unsafeRunSync() match {
      case Left(e) =>
        e shouldEqual EmptyFieldError("employee name must be non empty")
      case Right(_) => fail("expected some error")
    }
  }

  "findEmployeeLogic" should "return info about created employee with given token" in {
    employeeLogic.findEmployeeLogic(token).unsafeRunSync() match {
      case Left(e) => fail(s"unexpected error $e")
      case Right(info) =>
        info.name shouldEqual name
        info.level shouldEqual level
    }
  }

  it should "return an error if token doesn't exist" in {
    employeeLogic
      .findEmployeeLogic("not existing token")
      .unsafeRunSync() match {
      case Left(e) =>
        e shouldEqual EmployeeNotFound("no such employee with given token")
      case Right(_) => fail("expected some error")
    }
  }

  "advanceEmployeeLogic" should "increase employee level" in {
    employeeLogic.advanceEmployeeLogic(token).unsafeRunSync() match {
      case Left(e) => fail(s"unexpected error $e")
      case Right(info) =>
        info.name shouldEqual name
        info.level shouldEqual level + 1
    }
  }

  "delEmployeeLogic" should "delete employee with given token" in {
    employeeLogic.delEmployeeLogic(token).unsafeRunSync() match {
      case Left(e) => fail(s"unexpected error $e")
      case Right(info) =>
        info.name shouldEqual name
        info.level shouldEqual level + 1
    }
  }

  it should "return an error when repeated request" in {
    employeeLogic.delEmployeeLogic(token).unsafeRunSync() match {
      case Left(e) =>
        e shouldEqual EmployeeNotFound("no such employee with given token")
      case Right(_) => fail("expected some error")
    }
  }

}
