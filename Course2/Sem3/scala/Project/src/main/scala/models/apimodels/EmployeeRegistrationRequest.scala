package models.apimodels

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema

final case class EmployeeRegistrationRequest(name: String, level: Int)

object EmployeeRegistrationRequest {

  implicit val schema: Schema[EmployeeRegistrationRequest]   = Schema.derived
  implicit val encoder: Encoder[EmployeeRegistrationRequest] = deriveEncoder
  implicit val decoder: Decoder[EmployeeRegistrationRequest] = deriveDecoder

}
