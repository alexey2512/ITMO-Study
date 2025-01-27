package io

import io.MyIO._

import scala.language.implicitConversions
import scala.util.Try

/** Класс типов, позволяющий комбинировать описания вычислений, которые могут либо успешно
  * завершиться с некоторым значением, либо завершиться неуспешно, выбросив исключение Throwable.
  * @tparam F
  *   \- тип вычисления
  */
trait Computation[F[_]] {

  def map[A, B](fa: F[A])(f: A => B): F[B]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  def tailRecM[A, B](a: A)(f: A => F[Either[A, B]]): F[B]
  def pure[A](a: A): F[A]
  def *>[A, B](fa: F[A])(another: F[B]): F[B]
  def as[A, B](fa: F[A])(newValue: => B): F[B]
  def void[A](fa: F[A]): F[Unit]
  def attempt[A](fa: F[A]): F[Either[Throwable, A]]
  def option[A](fa: F[A]): F[Option[A]]

  /** Если вычисление fa выбрасывает ошибку, то обрабатывает ее функцией f, без изменения типа
    * выходного значения.
    * @return
    *   результат вычисления fa или результат функции f
    */
  def handleErrorWith[A, AA >: A](fa: F[A])(f: Throwable => F[AA]): F[AA]

  /** Обрабатывает ошибку вычисления чистой функцией recover или преобразует результат вычисления
    * чистой функцией.
    * @return
    *   результат вычисления преобразованный функцией map или результат функции recover
    */
  def redeem[A, B](fa: F[A])(recover: Throwable => B, map: A => B): F[B]
  def redeemWith[A, B](fa: F[A])(recover: Throwable => F[B], bind: A => F[B]): F[B]

  /** Выполняет вычисление. "unsafe", потому что при неуспешном завершении может выбросить
    * исключение.
    * @param fa
    *   \- еще не начавшееся вычисление
    * @tparam A
    *   \- тип результата вычисления
    * @return
    *   результат вычисления, если оно завершится успешно.
    */
  def unsafeRunSync[A](fa: F[A]): A

  /** Оборачивает ошибку в контекст вычисления.
    * @param error
    *   \- ошибка
    * @tparam A
    *   \- тип результата вычисления. Т.к. вычисление сразу завершится ошибкой при выполнении, то
    *   может быть любым.
    * @return
    *   создает описание вычисления, которое сразу же завершается с поданной ошибкой.
    */
  def raiseError[A](error: Throwable): F[A]

}

object Computation {
  def apply[F[_]: Computation]: Computation[F] = implicitly[Computation[F]]
}

final class MyIO[A] private (private val compute: Compute[A]) { self =>

  def map[B](f: A => B)(implicit
    comp: Computation[MyIO]
  ): MyIO[B] = comp.map(self)(f)

  def flatMap[B](f: A => MyIO[B])(implicit
    comp: Computation[MyIO]
  ): MyIO[B] = comp.flatMap(self)(f)

  def tailRecM[B](f: A => MyIO[Either[A, B]])(implicit
    comp: Computation[MyIO]
  ): MyIO[B] =
    Suspended(() => comp.tailRecM(self.run)(f))

  def *>[B](another: MyIO[B])(implicit
    comp: Computation[MyIO]
  ): MyIO[B] = comp.*>(self)(another)

  def as[B](newValue: => B)(implicit
    comp: Computation[MyIO]
  ): MyIO[B] = comp.as(self)(newValue)

  def void(implicit
    comp: Computation[MyIO]
  ): MyIO[Unit] = comp.void(self)

  def attempt(implicit
    comp: Computation[MyIO]
  ): MyIO[Either[Throwable, A]] = comp.attempt(self)

  def option(implicit
    comp: Computation[MyIO]
  ): MyIO[Option[A]] = comp.option(self)

  def handleErrorWith[AA >: A](f: Throwable => MyIO[AA])(implicit
    comp: Computation[MyIO]
  ): MyIO[AA] =
    comp.handleErrorWith[A, AA](self)(f)

  def redeem[B](recover: Throwable => B, map: A => B)(implicit
    comp: Computation[MyIO]
  ): MyIO[B] =
    comp.redeem(self)(recover, map)

  def redeemWith[B](recover: Throwable => MyIO[B], bind: A => MyIO[B])(implicit
    comp: Computation[MyIO]
  ): MyIO[B] =
    comp.redeemWith(self)(recover, bind)

  def unsafeRunSync(implicit
    comp: Computation[MyIO]
  ): A = comp.unsafeRunSync(self)

}

object MyIO {

  private sealed trait Compute[+A] { self =>

    def step: Either[Compute[A], A]

    def run: A = step match {
      case Right(v)      => v
      case Left(Fail(e)) => throw e
      case Left(more)    => more.run
    }

  }

  private final case class Pure[A](v: A) extends Compute[A] {
    override def step: Either[Compute[A], A] = Right(v)
  }

  private final case class Fail[A](e: Throwable) extends Compute[A] { self =>
    override def step: Either[Compute[A], A] = Left(self)
  }

  private final case class Suspended[A](call: () => Compute[A]) extends Compute[A] {
    override def step: Either[Compute[A], A] = Left(call())
  }

  private final case class FlatMap[A, B](sub: Compute[A], cont: A => Compute[B])
      extends Compute[B] {

    override def step: Either[Compute[B], B] = sub match {
      case Pure(v)         => Left(cont(v))
      case Fail(e)         => Left(Fail(e))
      case Suspended(call) => Left(FlatMap(call(), cont))
      case FlatMap(sub_, cont_) =>
        Left(FlatMap(sub_, (x: Any) => FlatMap(cont_(x), cont): Compute[B]))
      case Redeem(sub_, recover_, bind_) =>
        Left(Redeem(sub_, e => FlatMap(recover_(e), cont), (x: Any) => FlatMap(bind_(x), cont)))
    }

  }

  private final case class Redeem[A, B](
    sub: Compute[A],
    recover: Throwable => Compute[B],
    bind: A => Compute[B]
  ) extends Compute[B] {

    override def step: Either[Compute[B], B] = sub match {
      case Pure(v)         => Left(bind(v))
      case Fail(e)         => Left(recover(e))
      case Suspended(call) => Left(Redeem(call(), recover, bind))
      case FlatMap(sub_, cont_) =>
        Left(Redeem(sub_, recover, (x: Any) => Redeem(cont_(x), recover, bind)))
      case red @ Redeem(_, _, _) => Left(FlatMap(red, bind))
    }

  }

  private implicit def ComputeToIO[A](compute: Compute[A]): MyIO[A] = new MyIO[A](compute)
  private implicit def IOtoCompute[A](io: MyIO[A]): Compute[A]      = io.compute

  implicit val computationInstanceForIO: Computation[MyIO] = new Computation[MyIO] {
    override def map[A, B](fa: MyIO[A])(f: A => B): MyIO[B] = FlatMap(fa, (a: A) => pure[B](f(a)))
    override def flatMap[A, B](fa: MyIO[A])(f: A => MyIO[B]): MyIO[B] =
      FlatMap(fa, f.andThen(_.compute))
    override def tailRecM[A, B](a: A)(f: A => MyIO[Either[A, B]]): MyIO[B] = {
      def step(value: A): MyIO[B] = Suspended { () =>
        f(value).compute match {
          case Pure(Right(b))   => Pure(b)
          case Pure(Left(next)) => step(next)
          case Fail(e)          => Fail(e)
          case other =>
            FlatMap(
              other,
              (x: Either[A, B]) =>
                x match {
                  case Right(b) => Pure(b)
                  case Left(n)  => step(n)
                }
            )
        }
      }
      step(a)
    }
    override def pure[A](a: A): MyIO[A]                           = Pure(a)
    override def *>[A, B](fa: MyIO[A])(another: MyIO[B]): MyIO[B] = flatMap(fa)(_ => another)
    override def as[A, B](fa: MyIO[A])(newValue: => B): MyIO[B]   = map(fa)(_ => newValue)
    override def void[A](fa: MyIO[A]): MyIO[Unit]                 = *>(fa)(unit)
    override def redeemWith[A, B](
      fa: MyIO[A]
    )(recover: Throwable => MyIO[B], bind: A => MyIO[B]): MyIO[B] =
      Redeem(fa, recover.andThen(_.compute), bind.andThen(_.compute))
    override def attempt[A](fa: MyIO[A]): MyIO[Either[Throwable, A]] =
      redeemWith(fa)(e => Pure(Left(e)), (x: A) => Pure(Right(x)))
    override def option[A](fa: MyIO[A]): MyIO[Option[A]] =
      redeemWith(fa)(_ => none, (x: A) => Pure(Some(x)))
    override def redeem[A, B](fa: MyIO[A])(recover: Throwable => B, map: A => B): MyIO[B] =
      redeemWith(fa)(e => Pure(recover(e)), (x: A) => Pure(map(x)))
    override def handleErrorWith[A, AA >: A](fa: MyIO[A])(f: Throwable => MyIO[AA]): MyIO[AA] =
      redeemWith(fa)(f, a => Pure(a))
    override def unsafeRunSync[A](fa: MyIO[A]): A         = fa.run
    override def raiseError[A](error: Throwable): MyIO[A] = Fail(error)
  }

  def apply[A](body: => A): MyIO[A] = Suspended(() => Try(body).fold(Fail(_), Pure(_)))

  def suspend[A](thunk: => MyIO[A]): MyIO[A] =
    Suspended(() => Try(thunk).fold(Fail(_), identity[MyIO[A]]): Compute[A])

  def delay[A](body: => A): MyIO[A] = apply(body)

  def pure[A](a: A): MyIO[A] = Pure(a)

  def fromEither[A](e: Either[Throwable, A])(implicit
    comp: Computation[MyIO]
  ): MyIO[A] = e match {
    case Right(value) => pure(value)
    case Left(error)  => comp.raiseError(error)
  }

  def fromOption[A](option: Option[A])(orElse: => Throwable)(implicit
    comp: Computation[MyIO]
  ): MyIO[A] = fromEither(option.toRight(orElse))(comp)

  def fromTry[A](t: Try[A])(implicit
    comp: Computation[MyIO]
  ): MyIO[A] = fromEither(t.toEither)(comp)

  def none[A]: MyIO[Option[A]] = pure(None)

  def raiseUnless(cond: Boolean)(e: => Throwable)(implicit
    comp: Computation[MyIO]
  ): MyIO[Unit] =
    unlessA(cond)(comp.raiseError(e))

  def raiseWhen(cond: Boolean)(e: => Throwable)(implicit
    comp: Computation[MyIO]
  ): MyIO[Unit] =
    whenA(cond)(comp.raiseError(e))

  def raiseError[A](error: Throwable)(implicit
    comp: Computation[MyIO]
  ): MyIO[A] = comp.raiseError(error)

  def unlessA(cond: Boolean)(action: => MyIO[Unit]): MyIO[Unit] = if (cond) unit else action

  def whenA(cond: Boolean)(action: => MyIO[Unit]): MyIO[Unit] = unlessA(!cond)(action)

  val unit: MyIO[Unit] = pure(())

}
