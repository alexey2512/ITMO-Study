package database

import cats.effect.{IO, Resource}
import pureconfig.module.catseffect.syntax._
import doobie.hikari.HikariTransactor
import pureconfig.ConfigSource
import config.AppConfig
import scala.concurrent.ExecutionContext

object Transactor {

  def transactor: Resource[IO, HikariTransactor[IO]] = {
    Resource.eval(ConfigSource.default.loadF[IO, AppConfig]()).flatMap { config =>
      HikariTransactor.newHikariTransactor[IO](
        config.database.driver,
        config.database.url,
        config.database.user,
        config.database.password,
        ExecutionContext.global
      )
    }
  }

}
