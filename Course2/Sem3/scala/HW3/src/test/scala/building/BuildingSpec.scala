package building

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BuildingSpec extends AnyFlatSpec with Matchers {

  val males: Array[Resident] =
    (10 until 70 by 10).toArray.map(Resident(_, Male)).collect { case Right(resident) => resident }

  val females: Array[Resident] =
    (10 until 70 by 10).toArray.map(Resident(_, Female)).collect { case Right(resident) =>
      resident
    }

  val floor6: Floor = CommonAttic

  val floor5: Floor =
    CommercialFloor(Array(Commercial("KFC"), Commercial("Surf Coffee")), Some(floor6))

  val floor4: Floor = ResidentialFloor(males(1), females(2), Some(floor5))

  val floor3: Floor =
    CommercialFloor(Array(Commercial("Harvest"), Commercial("Gold apple")), Some(floor4))

  val floor2: Floor = ResidentialFloor(males(2), males(3), Some(floor3))
  val floor1: Floor = ResidentialFloor(males(4), males(5), Some(floor2))

  val _floor8: Floor = CommercialAttic(Commercial("StarBax"))
  val _floor7: Floor = ResidentialFloor(females(0), females(1), Some(_floor8))
  val _floor6: Floor = ResidentialFloor(females(3), females(4), Some(_floor7))
  val _floor5: Floor = ResidentialFloor(males(0), females(5), Some(_floor6))

  val _floor4: Floor =
    CommercialFloor(
      Array(Commercial("bank"), Commercial("T-bank"), Commercial("Alpha-bank")),
      Some(_floor5)
    )

  val _floor3: Floor = CommercialFloor(Array(Commercial("noname")), Some(_floor4))

  val _floor2: Floor =
    CommercialFloor(Array(Commercial("IKEA"), Commercial("Health")), Some(_floor3))

  val _floor1: Floor = ResidentialFloor(males(5), females(5), Some(_floor2))

  // specs on constructor validations

  "Resident" should "be invalid if age is non positive" in {
    val invalid = Resident(-10, Male)
    invalid shouldBe Left(NonPositiveAgeError)
  }

  "Building apply" should "return NotExitAtAtticError if last floor is not attic" in {
    val failFloor2: Floor = CommercialFloor(Array(Commercial("a")), None)
    val failFloor1: Floor = ResidentialFloor(males(0), females(0), Some(failFloor2))
    Building("some", failFloor1) shouldBe Left(NotExitAtAtticError)
    Building("some", failFloor2) shouldBe Left(NotExitAtAtticError)
  }

  it should "return EmptyCommercialFloorError if commercial floor has no commercials" in {
    val failFloor2: Floor = CommercialFloor(Array(), Some(_floor1))
    val failFloor1: Floor = ResidentialFloor(males(0), females(0), Some(failFloor2))
    Building("some", failFloor1) shouldBe Left(EmptyCommercialFloorError)
  }

  it should "return correct building else" in {
    Building("a", floor1) should matchPattern { case Right(_: Building) => }
    Building("b", _floor1) should matchPattern { case Right(_: Building) => }
  }

  // specs on methods

  "countOldManFloors" should "return the number of men older than the specified age" in {
    Building("a", floor1) match {
      case Right(building) => Building.countOldManFloors(building, 30) shouldEqual 2
      case Left(error)     => fail(s"building validation failed: $error")
    }
  }

  it should "return 0 if there are no men older than the specified age" in {
    Building("a", _floor5) match {
      case Right(building) => Building.countOldManFloors(building, 30) shouldEqual 0
      case Left(error)     => fail(s"building validation failed: $error")
    }
  }

  it should "return 0 if there are no men at all in the building" in {
    Building("a", _floor6) match {
      case Right(building) => Building.countOldManFloors(building, 0) shouldEqual 0
      case Left(error)     => fail(s"building validation failed: $error")
    }
  }

  "womanMaxAge" should "find age of the oldest woman in the building" in {
    Building("a", floor1) match {
      case Right(building) => Building.womanMaxAge(building) shouldEqual Some(30)
      case Left(error)     => fail(s"building validation failed: $error")
    }
  }

  it should "return None if there are no women in the building" in {
    Building("a", floor5) match {
      case Right(building) => Building.womanMaxAge(building) shouldEqual None
      case Left(error)     => fail(s"building validation failed: $error")
    }
  }

  "countCommercial" should "return number of commercial establishments in the building" in {
    Building("a", _floor1) match {
      case Right(building) => Building.countCommercial(building) shouldEqual 7
      case Left(error)     => fail(s"building validation failed: $error")
    }
  }

  it should "return 0 if there are no commercial establishments in the building" in {
    Building("a", floor6) match {
      case Right(building) => Building.countCommercial(building) shouldEqual 0
      case Left(error)     => fail(s"building validation failed: $error")
    }
  }

  "countCommercialAvg" should "return average commercial establishments through array of buildings" in {
    val b1 = Building("a", floor1) match {
      case Right(building) => building
      case Left(error)     => fail(s"building validation failed: $error")
    }
    val b2 = Building("a", _floor1) match {
      case Right(building) => building
      case Left(error)     => fail(s"building validation failed: $error")
    }
    Building.countCommercialAvg(Array(b1, b2)) shouldEqual 5.5
  }

  it should "return 0 if there are no commercial establishments in the buildings" in {
    val b1 = Building("a", floor6) match {
      case Right(building) => building
      case Left(error)     => fail(s"building validation failed: $error")
    }
    val __floor2: Floor = CommonAttic
    val __floor1: Floor = ResidentialFloor(males(0), males(0), Some(__floor2))
    val b2 = Building("a", __floor1) match {
      case Right(building) => building
      case Left(error)     => fail(s"building validation failed: $error")
    }
    Building.countCommercialAvg(Array(b1, b2)) shouldEqual 0.0
  }

  it should "return 0 if there are no buildings" in {
    Building.countCommercialAvg(Array()) shouldEqual 0.0
  }

  "evenFloorsMenAvg" should "return average count of mens on even floors in the building" in {
    Building("a", floor1) match {
      case Right(building) => Building.evenFloorsMenAvg(building) shouldEqual 1.5
      case Left(error)     => fail(s"building validation failed: $error")
    }
  }

  it should "return 0 if there are no mens in the building" in {
    Building("a", _floor6) match {
      case Right(building) => Building.evenFloorsMenAvg(building) shouldEqual 0.0
      case Left(error)     => fail(s"building validation failed: $error")
    }
  }

}
