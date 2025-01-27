package ventilation

import scala.annotation.tailrec

object Vent {

  def solve1(degrees: List[Int], k: Int): Option[List[Int]] = degrees match {
    case Nil         => Some(Nil)
    case _ if k <= 0 => None
    case _           => Some(degrees.sliding(k).map(_.max).toList)
  }

  def solve2(degrees: List[Int], k: Int): Option[List[Int]] = {

    def fillPostfixMaxes(begin: List[Int]): List[Int] = {
      @tailrec
      def reverse(begin: List[Int], idx: Int, acc: List[Int]): List[Int] = idx match {
        case 0 => acc
        case _ =>
          begin match {
            case Nil          => acc
            case head :: tail => reverse(tail, idx - 1, head :: acc)
          }
      }
      @tailrec
      def fill(list: List[Int], acc: List[Int]): List[Int] = list match {
        case Nil => acc
        case head :: tail =>
          acc match {
            case hd :: _ if hd > head => fill(tail, hd :: acc)
            case _ :: _               => fill(tail, head :: acc)
            case Nil                  => fill(tail, head :: acc)
          }
      }
      fill(reverse(begin, k, Nil), Nil)
    }

    // all performances of .head and .tail are safe, it was proofed
    @tailrec
    def fold(cur: List[Int], front: List[Int], pre: List[Int], backMax: Int, acc: List[Int]): List[Int] = cur match {
      case Nil =>
        front match {
          case Nil     => fillPostfixMaxes(pre).head :: acc
          case hd :: _ => Math.max(backMax, hd) :: acc
        }
      case head :: tail =>
        front match {
          case Nil =>
            val _front = fillPostfixMaxes(pre)
            fold(tail, _front.tail, pre.tail, head, _front.head :: acc)
          case hd :: tl =>
            fold(tail, tl, pre.tail, Math.max(backMax, head), Math.max(backMax, hd) :: acc)
        }
    }

    @tailrec
    def move(cur: List[Int], k: Int): List[Int] = k match {
      case 0 => cur
      case _ =>
        cur match {
          case Nil       => Nil
          case _ :: tail => move(tail, k - 1)
        }
    }

    degrees match {
      case Nil         => Some(Nil)
      case _ if k <= 0 => None
      case _           => Some(fold(move(degrees, k), Nil, degrees, Int.MinValue, Nil).reverse)
    }
  }

}
