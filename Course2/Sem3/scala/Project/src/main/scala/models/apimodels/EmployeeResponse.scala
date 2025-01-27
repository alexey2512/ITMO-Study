package models.apimodels

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema
import models.Aliases._

final case class EmployeeResponse(id: EmployeeId, name: String, level: Int)

object EmployeeResponse {

  implicit val schema: Schema[EmployeeResponse]   = Schema.derived
  implicit val encoder: Encoder[EmployeeResponse] = deriveEncoder
  implicit val decoder: Decoder[EmployeeResponse] = deriveDecoder

}
