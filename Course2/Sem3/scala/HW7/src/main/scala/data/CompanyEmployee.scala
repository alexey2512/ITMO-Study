package data

import unmarshal.decoder.Decoder
import unmarshal.encoder.Encoder
import unmarshal.model.Json
import unmarshal.model.Json.{JsonArray, JsonObject}
import unmarshal.error.DecoderError
import unmarshal.error.DecoderError._

case class CompanyEmployee(
  employees: List[Employee]
)

object CompanyEmployee {

  implicit def companyEmployeeEncoder: Encoder[CompanyEmployee] = company =>
    JsonObject(
      Map(
        "employees" -> JsonArray(company.employees.map(implicitly[Encoder[Employee]].toJson(_)))
      )
    )

  implicit def companyEmployeeDecoder: Decoder[CompanyEmployee] = json => {
    // I wanted make it via for comprehension in json match but some difficulties with rethrowing error occurred
    @scala.annotation.tailrec
    def fold(
      emList: List[Json],
      acc: List[Employee],
      index: Int
    ): Either[DecoderError, List[Employee]] = emList match {
      case Nil => Right(acc.reverse)
      case head :: tail =>
        implicitly[Decoder[Employee]].fromJson(head) match {
          case Left(DecoderError(reason, field)) =>
            Left(DecoderError(reason, s"employees.$index.$field"))
          case Right(employee) => fold(tail, employee :: acc, index + 1)
        }
    }
    json match {
      case JsonObject(map) =>
        for {
          emJsonArray <- map.get("employees").toRight(fieldNotFound("employees"))
          emList <- emJsonArray match {
            case JsonArray(list) => Right(list)
            case _               => Left(fieldTypeMismatch("employees"))
          }
          ems <- fold(emList, Nil, 0)
        } yield CompanyEmployee(ems)
      case j => Left(wrongJson("given is not json object", j.toString))
    }

  }

}
