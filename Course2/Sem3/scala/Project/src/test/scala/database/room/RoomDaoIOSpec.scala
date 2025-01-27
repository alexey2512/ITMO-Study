package database.room

import cats.effect.unsafe.implicits.global
import cats.effect.IO
import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec
import database.Transactor.transactor
import error.DBError._
import models.Aliases._

class RoomDaoIOSpec extends AnyFlatSpec with Matchers {

  val roomDao: RoomDao[IO] = new RoomDaoIO(transactor)

  val name: String   = "RoomDaoIOSpec"
  val level: Int     = 3
  var roomId: RoomId = 0

  "registerRoom" should "register new room successfully" in {
    roomId = roomDao.registerRoom(name, level).unsafeRunSync() match {
      case Left(e)     => fail(s"unexpected error $e")
      case Right(info) => info.id
    }
  }

  "findRoom" should "find info about created room" in {
    roomDao.findRoom(roomId).unsafeRunSync() match {
      case Left(e) => fail(s"unexpected error $e")
      case Right(info) =>
        info.name shouldEqual name
        info.level shouldEqual level
    }
  }

  it should "return an error if there are no such rooms with given id" in {
    roomDao.findRoom(0).unsafeRunSync() match {
      case Left(e)  => e shouldEqual GetDataError
      case Right(_) => fail("expected some error")
    }
  }

  "deleteRoom" should "delete room with given id successfully" in {
    roomDao.deleteRoom(roomId).unsafeRunSync() match {
      case Left(e) => fail(s"unexpected error $e")
      case Right(info) =>
        info.name shouldEqual name
        info.level shouldEqual level
    }
  }

  it should "return an error when repeated query" in {
    roomDao.deleteRoom(roomId).unsafeRunSync() match {
      case Left(e)  => e shouldEqual DeleteDataError
      case Right(_) => fail("expected some error")
    }
  }

}
