package models.apimodels

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema

final case class AccessResponse(allowed: Boolean, employee: EmployeeResponse)

object AccessResponse {

  implicit val schema: Schema[AccessResponse]   = Schema.derived
  implicit val encoder: Encoder[AccessResponse] = deriveEncoder
  implicit val decoder: Decoder[AccessResponse] = deriveDecoder

}
