package models.apimodels

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema
import models.Aliases._

final case class EmployeeRegistrationResponse(id: EmployeeId, token: Token)

object EmployeeRegistrationResponse {

  implicit val schema: Schema[EmployeeRegistrationResponse]   = Schema.derived
  implicit val encoder: Encoder[EmployeeRegistrationResponse] = deriveEncoder
  implicit val decoder: Decoder[EmployeeRegistrationResponse] = deriveDecoder

}
