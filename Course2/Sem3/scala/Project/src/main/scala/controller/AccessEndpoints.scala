package controller

import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.model._
import io.circe.generic.auto._
import models.apimodels._
import error._
import error.ApiError._
import models.Aliases._

object AccessEndpoints {

  private val oneOfApiError = oneOf[ApiError](
    oneOfVariant(StatusCode.NotFound, jsonBody[EmployeeNotFound]),
    oneOfVariant(StatusCode.NotFound, jsonBody[RoomNotFound]),
    oneOfVariant(StatusCode.InternalServerError, jsonBody[InternalError]),
    oneOfVariant(StatusCode.BadRequest, jsonBody[InvalidRequestError])
  )

  val accessEndpoint: Endpoint[Unit, (RoomId, Direction, Token), ApiError, AccessResponse, Any] =
    endpoint.post
      .in("access" / path[RoomId]("roomId") / path[Direction]("direction"))
      .in(header[Token]("Token"))
      .out(jsonBody[AccessResponse])
      .errorOut(oneOfApiError)

}
