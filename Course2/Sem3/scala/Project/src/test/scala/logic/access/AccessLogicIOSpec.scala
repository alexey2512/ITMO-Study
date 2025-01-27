package logic.access

import cats.effect.unsafe.implicits.global
import cats.effect.IO
import org.scalatest._
import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec
import database.Transactor.transactor
import database.employee._
import database.room._
import database.access._
import logic.employee._
import logic.room._
import models.apimodels._
import models.Aliases._

class AccessLogicIOSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll {

  val employeeDAO: EmployeeDao[IO]     = new EmployeeDaoIO(transactor)
  val roomDAO: RoomDao[IO]             = new RoomDaoIO(transactor)
  val accessDAO: AccessDao[IO]         = new AccessDaoIO(transactor)
  val employeeLogic: EmployeeLogic[IO] = new EmployeeLogicIO(employeeDAO)
  val roomLogic: RoomLogic[IO]         = new RoomLogicIO(roomDAO)

  val accessLogic: AccessLogic[IO] =
    new AccessLogicIO(employeeDAO, roomDAO, accessDAO)

  val ordinaryName: String = "ordinary employee"
  val ordinaryLevel: Int   = 1
  var ordinaryToken: Token = ""

  val bossName: String = "boss"
  val bossLevel: Int   = 10
  var bossToken: Token = ""

  val mainEntranceName: String = "main entrance"
  val mainEntranceLevel: Int   = 1
  var mainEntranceId: RoomId   = 0

  val bossOfficeName: String = "boss office"
  val bossOfficeLevel: Int   = 9
  var bossOfficeId: RoomId   = 0

  def createEmployee(name: String, level: Int): Token =
    employeeLogic
      .registerEmployeeLogic(EmployeeRegistrationRequest(name, level))
      .unsafeRunSync() match {
      case Left(e)     => fail(s"unexpected error $e")
      case Right(info) => info.token
    }

  def deleteEmployee(token: Token): Unit =
    employeeLogic.delEmployeeLogic(token).unsafeRunSync() match {
      case Left(e)  => fail(s"unexpected error $e")
      case Right(_) => ()
    }

  def createRoom(name: String, level: Int): RoomId =
    roomLogic
      .registerRoomLogic(RoomRegistrationRequest(name, level))
      .unsafeRunSync() match {
      case Left(e)     => fail(s"unexpected error $e")
      case Right(info) => info.id
    }

  def deleteRoom(id: RoomId): Unit =
    roomLogic.delRoomLogic(id).unsafeRunSync() match {
      case Left(e)  => fail(s"unexpected error $e")
      case Right(_) => ()
    }

  def accessTest(
    roomId: RoomId,
    direction: Direction,
    token: Token,
    proc: Boolean => Boolean
  ): Assertion =
    accessLogic.accessLogic((roomId, direction, token)).unsafeRunSync() match {
      case Left(e)    => fail(s"access logic failed with error: $e")
      case Right(res) => assert(proc(res.allowed))
    }

  def failedAccessTest(
    roomId: RoomId,
    direction: Direction,
    token: Token
  ): Assertion =
    accessLogic.accessLogic((roomId, direction, token)).unsafeRunSync() match {
      case Left(_)    => succeed
      case Right(res) => fail(s"expected error but found some response: $res")
    }

  override def beforeAll(): Unit = {
    super.beforeAll()
    ordinaryToken = createEmployee(ordinaryName, ordinaryLevel)
    bossToken = createEmployee(bossName, bossLevel)
    mainEntranceId = createRoom(mainEntranceName, mainEntranceLevel)
    bossOfficeId = createRoom(bossOfficeName, bossOfficeLevel)
  }

  "accessLogic" should "give first in-access if level affords" in {
    accessTest(mainEntranceId, "In", ordinaryToken, b => b)
  }

  it should "give out-access after in-access" in {
    accessTest(mainEntranceId, "Out", ordinaryToken, b => b)
  }

  it should "reject repeated attempt after success" in {
    accessTest(mainEntranceId, "Out", ordinaryToken, b => !b)
  }

  it should "reject access if level not afford" in {
    accessTest(bossOfficeId, "In", ordinaryToken, b => !b)
  }

  it should "return an error if there is no rooms with given id" in {
    failedAccessTest(-1, "In", bossToken)
  }

  it should "return an error if there is no employees with given token" in {
    failedAccessTest(mainEntranceId, "Out", "not existing token")
  }

  it should "return an error if direction is not one of expected variants" in {
    failedAccessTest(bossOfficeId, "yei", bossToken)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    deleteEmployee(ordinaryToken)
    deleteEmployee(bossToken)
    deleteRoom(mainEntranceId)
    deleteRoom(bossOfficeId)
  }

}
