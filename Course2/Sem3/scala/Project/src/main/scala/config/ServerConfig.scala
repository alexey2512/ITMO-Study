package config

import pureconfig._
import pureconfig.generic.semiauto._

final case class ServerConfig(
  host: String,
  port: Int
)

object ServerConfig {
  implicit val serverReader: ConfigReader[ServerConfig] = deriveReader
}
