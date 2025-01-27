package config

import pureconfig._
import pureconfig.generic.semiauto._

final case class DatabaseConfig(
  url: String,
  driver: String,
  user: String,
  password: String
)

object DatabaseConfig {
  implicit val databaseReader: ConfigReader[DatabaseConfig] = deriveReader
}
