package database

import error.DBError
import error.DBError._

private[database] object DaoPrivateCommons {

  def specifyWith[R](
    ei: Either[Throwable, Option[R]],
    er: DBError
  ): Either[DBError, R] =
    ei match {
      case Left(_)            => Left(ConnectionError)
      case Right(None)        => Left(er)
      case Right(Some(value)) => Right(value)
    }

}
