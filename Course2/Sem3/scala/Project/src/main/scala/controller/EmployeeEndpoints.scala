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

object EmployeeEndpoints {

  private val oneOfApiError = oneOf[ApiError](
    oneOfVariant(StatusCode.NotFound, jsonBody[EmployeeNotFound]),
    oneOfVariant(StatusCode.InternalServerError, jsonBody[InternalError]),
    oneOfVariant(StatusCode.BadRequest, jsonBody[EmptyFieldError])
  )

  val employeeRegisterEndpoint: Endpoint[Unit, EmployeeRegistrationRequest, ApiError, EmployeeRegistrationResponse, Any] =
    endpoint.post
      .in("employee" / "register")
      .in(jsonBody[EmployeeRegistrationRequest])
      .out(jsonBody[EmployeeRegistrationResponse])
      .errorOut(oneOfApiError)

  val employeeInfoEndpoint: Endpoint[Unit, Token, ApiError, EmployeeResponse, Any] =
    endpoint.get
      .in("employee" / "info")
      .in(header[Token]("Token"))
      .out(jsonBody[EmployeeResponse])
      .errorOut(oneOfApiError)

  val employeeAdvanceEndpoint: Endpoint[Unit, Token, ApiError, EmployeeResponse, Any] =
    endpoint.put
      .in("employee" / "advance")
      .in(header[Token]("Token"))
      .out(jsonBody[EmployeeResponse])
      .errorOut(oneOfApiError)

  val employeeDeleteEndpoint: Endpoint[Unit, Token, ApiError, EmployeeResponse, Any] =
    endpoint.delete
      .in("employee" / "delete")
      .in(header[Token]("Token"))
      .out(jsonBody[EmployeeResponse])
      .errorOut(oneOfApiError)

}
