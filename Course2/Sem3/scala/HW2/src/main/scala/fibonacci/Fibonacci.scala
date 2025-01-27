package fibonacci

import scala.annotation.tailrec

object Fibonacci {

  def fibonacci(limit: Long): BigInt = {

    def multiply2x2(m1: Array[Array[BigInt]], m2: Array[Array[BigInt]]): Array[Array[BigInt]] =
      Array(
        Array(
          m1(0)(0) * m2(0)(0) + m1(0)(1) * m2(1)(0),
          m1(0)(0) * m2(0)(1) + m1(0)(1) * m2(1)(1)
        ),
        Array(
          m1(1)(0) * m2(0)(0) + m1(1)(1) * m2(1)(0),
          m1(1)(0) * m2(0)(1) + m1(1)(1) * m2(1)(1)
        )
      )

    @tailrec
    def fastFibonacci(base: Array[Array[BigInt]], exp: Long, acc: Array[Array[BigInt]]): BigInt =
      if (exp == 0) acc(0)(1)
      else if ((exp & 1) == 1)
        fastFibonacci(multiply2x2(base, base), exp / 2, multiply2x2(acc, base))
      else fastFibonacci(multiply2x2(base, base), exp / 2, acc)

    val zero = BigInt(0)
    val one  = BigInt(1)

    fastFibonacci(
      Array(Array(zero, one), Array(one, one)),
      limit,
      Array(Array(one, zero), Array(zero, one))
    )
  }

}
