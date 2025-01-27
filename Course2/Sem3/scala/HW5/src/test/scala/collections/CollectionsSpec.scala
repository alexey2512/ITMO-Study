package collections

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Collections._

class CollectionsSpec extends AnyFlatSpec with Matchers {

  val map1: Map[String, Int] = Map("abc" -> 3, "def" -> 1, "ghi" -> 3)
  val map2: Map[String, Int] = Map("a" -> -10, "b" -> -15, "c" -> -5, "d" -> -15)
  val map3: Map[String, Int] = Map("a" -> 1)

  "findGaps" should "return Some() when l.length >= 2 and there are at least one pair" in {
    findGaps(Seq(1, 2, 8)) shouldEqual Some(Seq((2, 8)))
    findGaps(Seq(3, 5, 7)) shouldEqual Some(Seq((3, 5), (5, 7)))
    findGaps(List(4, 6, 10)) shouldEqual Some(Seq((4, 6), (6, 10)))
    findGaps(1 to 7 by 3) shouldEqual Some(Seq((1, 4), (4, 7)))
  }

  it should "return None else" in {
    findGaps(Seq(1, 2, 3, 4)) shouldEqual None
    findGaps(Seq()) shouldEqual None
    findGaps(List(1)) shouldEqual None
  }

  "minFold, minReduce, minRecursion" should "return the same result when map is non-empty" in {
    val testCases = Seq(
      (map1, Some(("def", 1))),
      (map2, Some(("b", -15))),
      (map3, Some(("a", 1)))
    )
    for ((map, expected) <- testCases) {
      minFold(map) shouldEqual expected
      minReduce(map) shouldEqual expected
      minRecursion(map) shouldEqual expected
    }
  }

  "scanLeft" should "return some interesting when list is non empty" in {
    scanLeft(List(1))(Seq(List(2)))(_ concat _) shouldEqual Seq(List(1, 2))
    scanLeft(0)(Vector(1, 2, 3))(_ + _) shouldEqual Seq(1, 3, 6)
    scanLeft(10)(1 to 4)(_ - _) shouldEqual Seq(9, 7, 4, 0)
    scanLeft("")(List("a", "b", "c"))(_ + _) shouldEqual Seq("a", "ab", "abc")
  }

  it should "return Nil else" in {
    scanLeft(0)(Seq())(_ + _).size shouldEqual 0
  }

  "count" should "return list of consistent char occurrences with their length" in {
    count("") shouldEqual List()
    count("abc") shouldEqual List(('a', 1), ('b', 1), ('c', 1))
    count("aaaaaa") shouldEqual List(('a', 6))
    count("aabbaa") shouldEqual List(('a', 2), ('b', 2), ('a', 2))
    count("abbccc") shouldEqual List(('a', 1), ('b', 2), ('c', 3))
  }

}
