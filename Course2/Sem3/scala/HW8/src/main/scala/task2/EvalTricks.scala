package task2

import cats.Eval

object EvalTricks {

  def fib(n: Int): Eval[BigInt] = {
    case class Matrix(arr: Array[BigInt]) {
      def *(other: Matrix): Matrix = Matrix(
        Array(
          arr(0) * other.arr(0) + arr(1) * other.arr(2),
          arr(0) * other.arr(1) + arr(1) * other.arr(3),
          arr(2) * other.arr(0) + arr(3) * other.arr(2),
          arr(2) * other.arr(1) + arr(3) * other.arr(3)
        )
      )
    }
    def loop(base: Matrix, exp: Int, acc: Matrix): Eval[BigInt] = {
      if (exp == 0) Eval.now(acc.arr(1))
      else if ((exp & 1) == 1) Eval.defer(loop(base * base, exp / 2, acc * base))
      else Eval.defer(loop(base * base, exp / 2, acc))
    }
    val zer = BigInt(0)
    val one = BigInt(1)
    loop(Matrix(Array(zer, one, one, one)), n, Matrix(Array(one, zer, zer, one)))
  }

  def foldRight[A, B](as: List[A], acc: Eval[B])(fn: (A, Eval[B]) => Eval[B]): Eval[B] = as match {
    case Nil          => acc
    case head :: tail => Eval.defer(fn(head, foldRight(tail, acc)(fn)))
  }

}
