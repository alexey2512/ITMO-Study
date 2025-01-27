package logic.room

import cats.effect.unsafe.implicits.global
import cats.effect.IO
import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec
import database.Transactor.transactor
import database.room._
import error.ApiError.EmptyFieldError
import models.Aliases._
import models.apimodels._
import error.ApiError._

class RoomLogicIOSpec extends AnyFlatSpec with Matchers {

  val roomDao: RoomDao[IO]     = new RoomDaoIO(transactor)
  val roomLogic: RoomLogic[IO] = new RoomLogicIO(roomDao)

  val name: String   = "RoomLogicIOSpec"
  val level: Int     = 10
  var roomId: RoomId = 0

  "registerRoomLogic" should "register new room successfully" in {
    roomId = roomLogic
      .registerRoomLogic(RoomRegistrationRequest(name, level))
      .unsafeRunSync() match {
      case Left(e)     => fail(s"unexpected error $e")
      case Right(info) => info.id
    }
  }

  it should "reject registration if name is empty" in {
    roomLogic
      .registerRoomLogic(RoomRegistrationRequest(" \n\n ", 1))
      .unsafeRunSync() match {
      case Left(e) =>
        e shouldEqual EmptyFieldError("room name must be non empty")
      case Right(_) => fail("expected some error")
    }
  }

  "delRoomLogic" should "delete room with given id" in {
    roomLogic.delRoomLogic(roomId).unsafeRunSync() match {
      case Left(e) => fail(s"unexpected error $e")
      case Right(info) =>
        info.name shouldEqual name
        info.level shouldEqual level
    }
  }

  it should "return an error when repeated request" in {
    roomLogic.delRoomLogic(roomId).unsafeRunSync() match {
      case Left(e)  => e shouldEqual RoomNotFound("no such room with given id")
      case Right(_) => fail("expected some error")
    }
  }

}
