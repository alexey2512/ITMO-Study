package unmarshal.encoder

import unmarshal.model.Json
import unmarshal.model.Json._
import shapeless.labelled.FieldType
import shapeless.{HList, HNil, ::, LabelledGeneric, Lazy, Witness}

trait Encoder[A] {
  def toJson(value: A): Json
}

object Encoder {

  def apply[A](implicit
    encoder: Encoder[A]
  ): Encoder[A] = encoder

  implicit val longToJson: Encoder[Long]       = long => JsonNum(long)
  implicit val doubleToJson: Encoder[Double]   = double => JsonDouble(double)
  implicit val booleanToJson: Encoder[Boolean] = boolean => JsonBool(boolean)
  implicit val stringToJson: Encoder[String]   = string => JsonString(string)

  implicit def listToJson[A](implicit
    encoder: Encoder[A]
  ): Encoder[List[A]] =
    (list: List[A]) => JsonArray(list.map(encoder.toJson))

  implicit def optionToJson[A](implicit
    encoder: Encoder[A]
  ): Encoder[Option[A]] = {
    case None        => JsonNull
    case Some(value) => encoder.toJson(value)
  }

  implicit val hNilEncoder: Encoder[HNil] = _ => JsonObject(Map.empty)

  implicit def hListEncoder[K <: Symbol, H, T <: HList](implicit
    witness: Witness.Aux[K],
    hEncoder: Lazy[Encoder[H]],
    tEncoder: Encoder[T]
  ): Encoder[FieldType[K, H] :: T] = { hList =>
    val name = witness.value.name
    val json = hEncoder.value.toJson(hList.head)
    val tail = tEncoder.toJson(hList.tail)
    tail match {
      case JsonObject(fields) => JsonObject(fields + (name -> json))
      case _                  => JsonObject(Map(name -> json))
    }
  }

  implicit def genericEncoder[A, R](implicit
    gen: LabelledGeneric.Aux[A, R],
    rEncoder: Lazy[Encoder[R]]
  ): Encoder[A] = (value: A) => rEncoder.value.toJson(gen.to(value))

  def autoDerive[A](implicit
    encoder: Encoder[A]
  ): Encoder[A] = encoder

}
