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

object RoomEndpoints {

  private val oneOfApiError = oneOf[ApiError](
    oneOfVariant(StatusCode.NotFound, jsonBody[RoomNotFound]),
    oneOfVariant(StatusCode.InternalServerError, jsonBody[InternalError]),
    oneOfVariant(StatusCode.BadRequest, jsonBody[EmptyFieldError])
  )

  val roomRegisterEndpoint: Endpoint[Unit, RoomRegistrationRequest, ApiError, RoomRegistrationResponse, Any] =
    endpoint.post
      .in("room" / "register")
      .in(jsonBody[RoomRegistrationRequest])
      .out(jsonBody[RoomRegistrationResponse])
      .errorOut(oneOfApiError)

  val roomDeleteEndpoint: Endpoint[Unit, RoomId, ApiError, RoomResponse, Any] =
    endpoint.delete
      .in("room" / "delete" / path[RoomId]("roomId"))
      .out(jsonBody[RoomResponse])
      .errorOut(oneOfApiError)

}
