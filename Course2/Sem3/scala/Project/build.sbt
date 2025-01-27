import sbt.*
import Dependencies.*

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

Compile / compile / scalacOptions ++= Seq(
  "-Werror",
  "-Wdead-code",
  "-Wextra-implicit",
  "-Wnumeric-widen",
  "-Wunused",
  "-Wvalue-discard",
  "-Xlint",
  "-Xlint:-byname-implicit",
  "-Xlint:-implicit-recursion",
  "-unchecked",
  "-feature"
)

Compile / run / fork := true

lazy val root = (project in file("."))
  .settings(
    name := "EmployeeAccessControl",
    libraryDependencies ++= Seq(
      logback_classic,
      postgres_driver,
      scalatest_scala
    ) ++ Tapir.libs
      ++ Circe.libs
      ++ Jwt.libs
      ++ Cats.libs
      ++ PureConfig.libs
      ++ Doobie.libs
      ++ Http4s.libs
  )
