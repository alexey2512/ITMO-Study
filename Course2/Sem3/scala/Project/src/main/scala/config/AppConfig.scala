package config

import pureconfig._
import pureconfig.generic.semiauto._

final case class AppConfig(
  name: String,
  version: String,
  database: DatabaseConfig,
  server: ServerConfig
)

object AppConfig {
  implicit val appReader: ConfigReader[AppConfig] = deriveReader
}
