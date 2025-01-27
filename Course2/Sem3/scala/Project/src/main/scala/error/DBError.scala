package error

sealed trait DBError extends Throwable

object DBError {

  object ConnectionError extends DBError
  object PostDataError   extends DBError
  object GetDataError    extends DBError
  object UpdateDataError extends DBError
  object DeleteDataError extends DBError

}
