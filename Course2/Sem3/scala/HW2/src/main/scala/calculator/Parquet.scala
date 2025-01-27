package calculator

import Building.*

object Parquet {

  def calculate(building: Building): Int =
    building match {
      case Economy(len, wid, hgt, flr) =>
        len * wid * hgt + flr * 10000
      case Premium(len, wid, hgt, flr) =>
        math.pow(if (flr < 5) 3 else 2, flr).toInt * (len + wid + hgt)
    }

}
