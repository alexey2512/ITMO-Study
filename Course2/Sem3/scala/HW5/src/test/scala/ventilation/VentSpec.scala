package ventilation

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Vent._

class VentSpec extends AnyFlatSpec with Matchers {

  val test1: List[Int] = List(1, 2, 3, 4)
  val test2: List[Int] = List(10, 4, 3, 9, 2, 1, 10, 12)
  val test3: List[Int] = List(23, 4, 4, 1, 1, 5, 45, 75)

  def testSolve(name: String, solve: (List[Int], Int) => Option[List[Int]]): Unit = {
    name should "return correct answer" in {
      solve(test1, 2) shouldEqual Some(List(2, 3, 4))
      solve(test2, 4) shouldEqual Some(List(10, 9, 9, 10, 12))
    }

    it should "identity this list if k = 1" in {
      solve(test1, 1) shouldEqual Some(test1)
      solve(test2, 1) shouldEqual Some(test2)
    }

    it should "return max in list if k = list.length" in {
      solve(test1, test1.size) shouldEqual Some(List(test1.max))
      solve(test2, test2.size) shouldEqual Some(List(test2.max))
    }

    it should "return empty list if list if empty" in {
      solve(List(), 2) shouldEqual Some(Nil)
    }

    it should "return None if list is non empty but k <= 0" in {
      solve(test1, 0) shouldEqual None
    }
  }

  testSolve("solve1", solve1)
  testSolve("solve2", solve2)

  "solve1/solve2" should "return equal results on equal in data" in {
    solve1(test3, 1) shouldEqual solve2(test3, 1).map(_.toList)
    solve1(test3, 2) shouldEqual solve2(test3, 2).map(_.toList)
    solve1(test3, 5) shouldEqual solve2(test3, 5).map(_.toList)
  }

}
