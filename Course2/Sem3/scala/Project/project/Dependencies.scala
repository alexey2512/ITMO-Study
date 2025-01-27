import sbt.*

object Dependencies {

  val logback_classic = "ch.qos.logback" % "logback-classic" % "1.5.6"
  val postgres_driver = "org.postgresql" % "postgresql" % "42.7.3"
  val scalatest_scala = "org.scalatest" %% "scalatest" % "3.2.19" % Test

  object Tapir {
    val version: String = "1.10.15"
    val libs: Seq[ModuleID] = Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-core" % version,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % version,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % version,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui" % version,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % version,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % version
    )
  }

  object Circe {
    val version: String = "0.14.9"
    val libs: Seq[ModuleID] = Seq(
      "io.circe" %% "circe-core" % version,
      "io.circe" %% "circe-generic" % version,
      "io.circe" %% "circe-parser" % version,
      "io.circe" %% "circe-optics" % "0.15.0"
    )
  }

  object Jwt {
    val version: String = "10.0.1"
    val libs: Seq[ModuleID] = Seq(
      "com.github.jwt-scala" %% "jwt-core" % version,
      "com.github.jwt-scala" %% "jwt-circe" % version
    )
  }

  object Cats {
    val libs: Seq[ModuleID] = Seq(
      "org.typelevel" %% "cats-core" % "2.12.0",
      "org.typelevel" %% "cats-effect" % "3.5.2"
    )
  }

  object PureConfig {
    val version: String = "0.17.8"
    val libs: Seq[ModuleID] = Seq(
      "com.github.pureconfig" %% "pureconfig" % version,
      "com.github.pureconfig" %% "pureconfig-core" % version,
      "com.github.pureconfig" %% "pureconfig-generic" % version,
      "com.github.pureconfig" %% "pureconfig-cats-effect" % version
    )
  }

  object Doobie {
    val version: String = "1.0.0-RC2"
    val libs: Seq[ModuleID] = Seq(
      "org.tpolecat" %% "doobie-core" % version,
      "org.tpolecat" %% "doobie-postgres" % version,
      "org.tpolecat" %% "doobie-hikari" % version
    )
  }

  object Http4s {
    val version: String = "0.23.27"
    val libs: Seq[ModuleID] = Seq(
      "org.http4s" %% "http4s-ember-server" % version,
      "org.http4s" %% "http4s-circe" % version
    )
  }

}
