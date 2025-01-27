import cats.effect.{IO, IOApp, ExitCode}
import org.http4s.ember.server.EmberServerBuilder
import com.comcast.ip4s._
import pureconfig._
import pureconfig.module.catseffect.syntax._
import config.AppConfig
import database.Transactor.transactor
import database.employee._
import database.room._
import database.access._
import logic.employee._
import logic.room._
import logic.access._
import controller.Router

object Application extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val employeeDAO: EmployeeDao[IO]     = new EmployeeDaoIO(transactor)
    val roomDAO: RoomDao[IO]             = new RoomDaoIO(transactor)
    val accessDAO: AccessDao[IO]         = new AccessDaoIO(transactor)
    val employeeLogic: EmployeeLogic[IO] = new EmployeeLogicIO(employeeDAO)
    val roomLogic: RoomLogic[IO]         = new RoomLogicIO(roomDAO)
    val accessLogic: AccessLogic[IO]     = new AccessLogicIO(employeeDAO, roomDAO, accessDAO)
    val router: Router                   = new Router(employeeLogic, roomLogic, accessLogic)
    ConfigSource.default
      .loadF[IO, AppConfig]()
      .flatMap(config =>
        router.httpRoutes.flatMap(routes =>
          EmberServerBuilder
            .default[IO]
            .withHost(Host.fromString(config.server.host).get)
            .withPort(Port.fromInt(config.server.port).get)
            .withHttpApp(routes.orNotFound)
            .build
            .use(_ => IO.never)
        )
      )
  }

}
