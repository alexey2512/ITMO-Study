package controller

import sttp.tapir.server._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import cats.effect.IO
import org.http4s.HttpRoutes
import pureconfig._
import pureconfig.module.catseffect.syntax._
import config.AppConfig
import logic.access.AccessLogic
import logic.employee.EmployeeLogic
import logic.room.RoomLogic
import AccessEndpoints._
import EmployeeEndpoints._
import RoomEndpoints._

final class Router(
  val employeeLogic: EmployeeLogic[IO],
  val roomLogic: RoomLogic[IO],
  val accessLogic: AccessLogic[IO]
) {

  private val swaggerEndpoints: IO[List[ServerEndpoint[Any, IO]]] =
    ConfigSource.default.loadF[IO, AppConfig]().flatMap { conf =>
      IO.pure(
        SwaggerInterpreter()
          .fromEndpoints[IO](
            List(
              employeeRegisterEndpoint,
              employeeInfoEndpoint,
              employeeAdvanceEndpoint,
              employeeDeleteEndpoint,
              roomRegisterEndpoint,
              roomDeleteEndpoint,
              accessEndpoint
            ),
            conf.name,
            conf.version
          )
      )
    }

  val httpRoutes: IO[HttpRoutes[IO]] = for {
    endpoints <- swaggerEndpoints
    routes = Http4sServerInterpreter[IO]().toRoutes(
      List(
        employeeRegisterEndpoint.serverLogic(
          employeeLogic.registerEmployeeLogic
        ),
        employeeInfoEndpoint.serverLogic(employeeLogic.findEmployeeLogic),
        employeeAdvanceEndpoint.serverLogic(employeeLogic.advanceEmployeeLogic),
        employeeDeleteEndpoint.serverLogic(employeeLogic.delEmployeeLogic),
        roomRegisterEndpoint.serverLogic(roomLogic.registerRoomLogic),
        roomDeleteEndpoint.serverLogic(roomLogic.delRoomLogic),
        accessEndpoint.serverLogic(accessLogic.accessLogic)
      ) ++ endpoints
    )
  } yield routes

}
