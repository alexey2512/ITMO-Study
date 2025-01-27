package logic.room

import error.ApiError
import models.apimodels._
import models.Aliases._

trait RoomLogic[F[_]] {

  def registerRoomLogic: RoomRegistrationRequest => F[Either[ApiError, RoomRegistrationResponse]]

  def delRoomLogic: RoomId => F[Either[ApiError, RoomResponse]]

}
