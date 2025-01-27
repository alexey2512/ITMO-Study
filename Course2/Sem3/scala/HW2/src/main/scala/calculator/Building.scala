package calculator

sealed trait Building(length: Int, width: Int, height: Int, floorNumber: Int)

object Building {

  private def check(len: Int, wid: Int, hgt: Int, flr: Int): Unit = {
    def positive(name: String, value: Int): Unit =
      if (value <= 0)
        throw new Errors.ArgumentShouldBePositiveException(
          s"$name must be positive, but found $value"
        )
    positive("length", len)
    positive("width", wid)
    positive("height", hgt)
    positive("floor number", flr)
  }

  final case class Economy(length: Int, width: Int, height: Int, floorNumber: Int)
      extends Building(length, width, height, floorNumber) {
    check(length, width, height, floorNumber)
  }

  final case class Premium(length: Int, width: Int, height: Int, floorNumber: Int)
      extends Building(length, width, height, floorNumber) {
    check(length, width, height, floorNumber)
  }

}
