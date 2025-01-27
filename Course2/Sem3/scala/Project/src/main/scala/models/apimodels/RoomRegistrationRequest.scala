package models.apimodels

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema

final case class RoomRegistrationRequest(name: String, level: Int)

object RoomRegistrationRequest {

  implicit val schema: Schema[RoomRegistrationRequest]   = Schema.derived
  implicit val encoder: Encoder[RoomRegistrationRequest] = deriveEncoder
  implicit val decoder: Decoder[RoomRegistrationRequest] = deriveDecoder

}
