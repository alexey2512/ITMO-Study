package building

sealed trait CheckError
case object NonPositiveAgeError       extends CheckError
case object NotExitAtAtticError       extends CheckError
case object EmptyCommercialFloorError extends CheckError

sealed trait Sex
case object Male   extends Sex
case object Female extends Sex

case class Resident(age: Int, sex: Sex)

object Resident {

  def apply(age: Int, sex: Sex): Either[CheckError, Resident] =
    if (age > 0) Right(new Resident(age, sex)) else Left(NonPositiveAgeError)

}

case class Commercial(name: String)

sealed trait Floor {
  def next: Option[Floor]
}

case class ResidentialFloor(resident1: Resident, resident2: Resident, next: Option[Floor])
    extends Floor

case class CommercialFloor(commercials: Array[Commercial], next: Option[Floor]) extends Floor

sealed trait Attic extends Floor {
  override def next: Option[Floor] = None
}

case object CommonAttic extends Attic

case class CommercialAttic(commercial: Commercial) extends Attic

case class Building(address: String, first: Floor)

object Building {

  def apply(address: String, first: Floor): Either[CheckError, Building] = {
    def validateFloor(floor: Floor): Either[CheckError, Floor] =
      floor match {
        case ResidentialFloor(_, _, Some(next)) => Right(next)
        case CommercialFloor(com, Some(next)) =>
          if (com.isEmpty) Left(EmptyCommercialFloorError) else Right(next)
        case _: Attic => Right(floor)
        case _        => Left(NotExitAtAtticError)
      }

    @scala.annotation.tailrec
    def validateBuilding(floor: Floor): Option[CheckError] =
      validateFloor(floor) match {
        case Left(error)     => Some(error)
        case Right(_: Attic) => None
        case Right(next)     => validateBuilding(next)
      }

    validateBuilding(first) match {
      case Some(error) => Left(error)
      case _           => Right(new Building(address, first))
    }
  }

  def fold(building: Building, accumulator: Int)(f: (Int, Floor) => Int): Int = {
    @scala.annotation.tailrec
    def walk(floor: Floor, acc: Int): Int =
      floor.next match {
        case Some(next) => walk(next, f(acc, floor))
        case None       => f(acc, floor)
      }
    walk(building.first, accumulator)
  }

  def countOldManFloors(building: Building, olderThan: Int): Int = fold(building, 0)((acc, floor) =>
    floor match {
      case ResidentialFloor(r1, r2, _)
          if (r1.sex == Male && r1.age > olderThan || r2.sex == Male && r2.age > olderThan) =>
        acc + 1
      case _ => acc
    }
  )

  def womanMaxAge(building: Building): Option[Int] = fold(building, 0) { (acc, floor) =>
    def getAge(res: Resident) = if (res.sex == Female) res.age else 0
    floor match {
      case ResidentialFloor(res1, res2, _) => math.max(getAge(res1), math.max(getAge(res2), acc))
      case _                               => acc
    }
  } match {
    case 0 => None
    case a => Some(a)
  }

  def countCommercial(building: Building): Int = fold(building, 0)((acc, floor) =>
    floor match {
      case CommercialFloor(com, _) => acc + com.length
      case CommercialAttic(_)      => acc + 1
      case _                       => acc
    }
  )

  // why we can't just reuse countCommercial function? we don't have to use new fold function
  def countCommercialAvg(buildings: Array[Building]): Double =
    if (buildings.nonEmpty) buildings.map(countCommercial).sum.toDouble / buildings.length else 0.0

  def otherFold(building: Building, accumulator: Double)(
    f: (Floor, Double, Int) => Double
  ): Double = {
    @scala.annotation.tailrec
    def walk(floor: Floor, acc: Double, index: Int): Double =
      floor.next match {
        case Some(next) => walk(next, f(floor, acc, index), index + 1)
        case None       => f(floor, acc, index)
      }
    walk(building.first, accumulator, 1)
  }

  def evenFloorsMenAvg(building: Building): Double = otherFold(building, 0) { (floor, acc, index) =>
    def getMale(res: Resident) = if (res.sex == Male) 1 else 0
    floor match {
      case ResidentialFloor(res1, res2, _) if index % 2 == 0 =>
        (acc * (index - 2) / 2 + getMale(res1) + getMale(res2)) / index * 2
      case _ => acc
    }
  }

}
