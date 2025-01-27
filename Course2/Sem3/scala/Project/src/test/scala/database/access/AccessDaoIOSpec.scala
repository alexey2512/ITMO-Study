package database.access

import cats.effect.unsafe.implicits.global
import cats.effect.IO
import org.scalatest._
import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec
import database.Transactor.transactor
import database.employee._
import database.room._
import error.DBError.PostDataError
import logic.employee.TokenGenerator.generateToken
import models.Aliases._

class AccessDaoIOSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll {

  val employeeDao: EmployeeDao[IO] = new EmployeeDaoIO(transactor)
  val roomDao: RoomDao[IO]         = new RoomDaoIO(transactor)
  val accessDao: AccessDao[IO]     = new AccessDaoIO(transactor)

  val employeeName: String   = "test access employee"
  val employeeLevel: Int     = 1
  val token: Token           = generateToken(employeeName).unsafeRunSync()
  var employeeId: EmployeeId = 0

  val roomName: String = "test access room"
  val roomLevel: Int   = 1
  var roomId: RoomId   = 0

  override def beforeAll(): Unit = {
    super.beforeAll()
    employeeId = employeeDao
      .registerEmployee(employeeName, employeeLevel, token)
      .unsafeRunSync() match {
      case Left(e)     => fail(s"unexpected error $e")
      case Right(info) => info.id
    }
    roomId = roomDao.registerRoom(roomName, roomLevel).unsafeRunSync() match {
      case Left(e)     => fail(s"unexpected error $e")
      case Right(info) => info.id
    }
  }

  "postAccessLog" should "successfully insert into db new log" in {
    accessDao
      .postAccessLog(employeeId, roomId, "In", allowed = true)
      .unsafeRunSync() match {
      case Left(e)        => fail(s"unexpected error $e")
      case Right(touched) => assert(touched > 0)
    }
  }

  it should "fail if direction is incorrect" in {
    accessDao
      .postAccessLog(employeeId, roomId, "abc", allowed = false)
      .unsafeRunSync() match {
      case Left(e)  => e shouldEqual PostDataError
      case Right(_) => fail("expected some error")
    }
  }

  "lastAccess" should "return last access direction" in {
    accessDao.lastAccess(employeeId, roomId).unsafeRunSync() match {
      case Left(e)    => fail(s"unexpected error $e")
      case Right(dir) => dir shouldEqual "In"
    }
  }

  override def afterAll(): Unit = {
    super.afterAll()
    employeeDao.deleteEmployee(token).unsafeRunSync() match {
      case Left(e)  => fail(s"unexpected error $e")
      case Right(_) => ()
    }
    roomDao.deleteRoom(roomId).unsafeRunSync() match {
      case Left(e)  => fail(s"unexpected error $e")
      case Right(_) => ()
    }
  }

}
