package complex

import scala.language.implicitConversions

// DO NOT CHANGE ANYTHING BELOW
final case class ComplexNumber(real: Double, imaginary: Double) {
  def *(other: ComplexNumber) =
    ComplexNumber(
      (real * other.real) - (imaginary * other.imaginary),
      (real * other.imaginary) + (imaginary * other.real)
    )

  def +(other: ComplexNumber) =
    ComplexNumber(real + other.real, imaginary + other.imaginary)

  def ~=(o: ComplexNumber) =
    (real - o.real).abs < 1e-6 && (imaginary - o.imaginary).abs < 1e-6
}

object ComplexNumber
// DO NOT CHANGE ANYTHING ABOVE

object ComplexNumberImplicitExtensions {

  // I think throwing an exception is not necessary if we already allow infinite and nan real/imaginary
  implicit class ComplexNumberExtendedOperations(val c: ComplexNumber) extends AnyVal {
    def -(o: ComplexNumber): ComplexNumber =
      ComplexNumber(c.real - o.real, c.imaginary - o.imaginary)

    def /(o: ComplexNumber): ComplexNumber = {
      val modulus2: Double = o.real * o.real + o.imaginary * o.imaginary
      ComplexNumber(
        (c.real * o.real + c.imaginary * o.imaginary) / modulus2,
        (c.imaginary * o.real - c.real * o.imaginary) / modulus2
      )
    }

    def toPolar: PolarComplexNumber =
      PolarComplexNumber(Math.hypot(c.real, c.imaginary), Math.atan2(c.imaginary, c.real))
  }

  implicit def realToComplex[T](num: T)(implicit nic: Numeric[T]): ComplexNumber =
    ComplexNumber(nic.toDouble(num), 0)

  implicit class ImaginaryToComplex[T](num: T)(implicit nic: Numeric[T]) {
    def i: ComplexNumber = ComplexNumber(0, nic.toDouble(num))
  }
}

final case class PolarComplexNumber(radius: Double, angle: Double) {
  def toNormal: ComplexNumber = ComplexNumber(radius * Math.cos(angle), radius * Math.sin(angle))
}

object Test extends App {
  import ComplexNumberImplicitExtensions._

  val x: ComplexNumber = 10
  val y: ComplexNumber = 2.5 + 3.5.i
  val z: ComplexNumber = 10.i - 5

  println(1 + ComplexNumber(1, 2))
  println(2.0 * ComplexNumber(-1, -1))
  println(ComplexNumber(2, 2) - 1)
  println(ComplexNumber(-2, -2) / 2.0)
}
