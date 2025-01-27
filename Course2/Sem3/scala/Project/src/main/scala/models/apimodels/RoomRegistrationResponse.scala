package models.apimodels

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema
import models.Aliases._

final case class RoomRegistrationResponse(id: RoomId)

object RoomRegistrationResponse {

  implicit val schema: Schema[RoomRegistrationResponse]   = Schema.derived
  implicit val encoder: Encoder[RoomRegistrationResponse] = deriveEncoder
  implicit val decoder: Decoder[RoomRegistrationResponse] = deriveDecoder

}
