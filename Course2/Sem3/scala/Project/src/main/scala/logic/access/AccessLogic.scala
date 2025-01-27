package logic.access

import error._
import models.apimodels._
import models.Aliases._

trait AccessLogic[F[_]] {

  def accessLogic: ((RoomId, Direction, Token)) => F[Either[ApiError, AccessResponse]]

}
