package error

sealed trait ApiError extends Throwable {
  def message: String
}

object ApiError {

  final case class EmployeeNotFound(message: String)    extends ApiError
  final case class RoomNotFound(message: String)        extends ApiError
  final case class InternalError(message: String)       extends ApiError
  final case class EmptyFieldError(message: String)     extends ApiError
  final case class InvalidRequestError(message: String) extends ApiError

}
