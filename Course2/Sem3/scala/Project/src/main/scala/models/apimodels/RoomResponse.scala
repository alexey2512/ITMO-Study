package models.apimodels

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema

final case class RoomResponse(name: String, level: Int)

object RoomResponse {

  implicit val schema: Schema[RoomResponse]   = Schema.derived
  implicit val encoder: Encoder[RoomResponse] = deriveEncoder
  implicit val decoder: Decoder[RoomResponse] = deriveDecoder

}
