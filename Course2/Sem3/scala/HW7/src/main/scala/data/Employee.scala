package data

import unmarshal.decoder.Decoder
import unmarshal.encoder.Encoder
import unmarshal.model.Json._
import unmarshal.error.DecoderError._

case class Employee(
  name: String,
  age: Long,
  id: Long,
  bossId: Option[Long]
)

object Employee {

  implicit def employeeEncoder: Encoder[Employee] = (em: Employee) =>
    JsonObject(
      Map(
        "name" -> JsonString(em.name),
        "age"  -> JsonNum(em.age),
        "id"   -> JsonNum(em.id),
        "bossId" -> (em.bossId match {
          case None        => JsonNull
          case Some(value) => JsonNum(value)
        })
      )
    )

  implicit def employeeDecoder: Decoder[Employee] = {
    case JsonObject(map) =>
      for {
        nameJson   <- map.get("name").toRight(fieldNotFound("name"))
        ageJson    <- map.get("age").toRight(fieldNotFound("age"))
        idJson     <- map.get("id").toRight(fieldNotFound("id"))
        bossIdJson <- map.get("bossId").toRight(fieldNotFound("bossId"))
        name <- nameJson match {
          case JsonString(name) => Right(name)
          case _                => Left(fieldTypeMismatch("name"))
        }
        age <- ageJson match {
          case JsonNum(age) => Right(age)
          case _            => Left(fieldTypeMismatch("age"))
        }
        id <- idJson match {
          case JsonNum(id) => Right(id)
          case _           => Left(fieldTypeMismatch("id"))
        }
        bossId <- bossIdJson match {
          case JsonNull        => Right(None)
          case JsonNum(bossId) => Right(Some(bossId))
          case _               => Left(fieldTypeMismatch("bossId"))
        }
        extraFields = map.keySet.diff(Set("name", "age", "id", "bossId"))
        result <-
          if (extraFields.isEmpty)
            Right(Employee(name, age, id, bossId))
          else
            Left(wrongJson("extra field found", extraFields.mkString(", ")))
      } yield result
    case j => Left(wrongJson("given is not json object", j.toString))
  }

}
