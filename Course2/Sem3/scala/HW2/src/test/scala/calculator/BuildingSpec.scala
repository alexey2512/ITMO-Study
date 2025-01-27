package calculator

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Errors.ArgumentShouldBePositiveException
import Building.*

class BuildingSpec extends AnyFlatSpec with Matchers {

  "Building" should "return error when length is non-positive" in {
    intercept[ArgumentShouldBePositiveException] {
      Economy(0, 1, 2, 3)
    }.getMessage shouldEqual "length must be positive, but found 0"
    intercept[ArgumentShouldBePositiveException] {
      Premium(0, 1, 2, 3)
    }.getMessage shouldEqual "length must be positive, but found 0"
  }

  it should "return error when width is non-positive" in {
    intercept[ArgumentShouldBePositiveException] {
      Economy(10, -10, 2, 3)
    }.getMessage shouldEqual "width must be positive, but found -10"
    intercept[ArgumentShouldBePositiveException] {
      Premium(10, -10, 2, 3)
    }.getMessage shouldEqual "width must be positive, but found -10"
  }

  it should "return error when height is non-positive" in {
    intercept[ArgumentShouldBePositiveException] {
      Economy(10, 20, -30, 40)
    }.getMessage shouldEqual "height must be positive, but found -30"
    intercept[ArgumentShouldBePositiveException] {
      Premium(10, 20, -30, 40)
    }.getMessage shouldEqual "height must be positive, but found -30"
  }

  it should "return error when floor number is non-positive" in {
    intercept[ArgumentShouldBePositiveException] {
      Economy(10, 20, 30, -40)
    }.getMessage shouldEqual "floor number must be positive, but found -40"
    intercept[ArgumentShouldBePositiveException] {
      Premium(10, 20, 30, -40)
    }.getMessage shouldEqual "floor number must be positive, but found -40"
  }

  it should "return error when any of parameters is non-positive" in {
    intercept[ArgumentShouldBePositiveException] {
      Economy(0, 0, 0, 0)
    }.getMessage shouldEqual "length must be positive, but found 0"
    intercept[ArgumentShouldBePositiveException] {
      Premium(0, 0, 0, 0)
    }.getMessage shouldEqual "length must be positive, but found 0"
  }

  it should "work correctly when all parameters are positive" in {
    noException should be thrownBy Economy(1, 1, 1, 1)
    noException should be thrownBy Premium(1, 1, 1, 1)
  }

}
