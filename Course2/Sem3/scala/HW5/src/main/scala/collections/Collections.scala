package collections

object Collections {

  /*
    In a sorted list find all pairs of two neighbor numbers which have a gap between them
    None for Seq(1, 2, 3, 4)
    Some(Seq((2, 8))) for Seq(1, 2, 8)
    Some(Seq((3, 5), (5, 7))) for Seq(3, 5, 7)
   */
  def findGaps(l: Seq[Int]): Option[Seq[(Int, Int)]] = {
    if (l.length < 2)
      None
    else
      Some(
        l.sliding(2)
          .collect {
            case Seq(a, b) if b - a >= 2 => (a, b)
          }
          .toSeq
      ).filter(_.nonEmpty)
  }

  /*
    Find key-value pair with the minimum value in the map
    try to implement min in different ways (fold, reduce, recursion)
   */
  def minFold(map: Map[String, Int]): Option[(String, Int)] = {
    if (map.isEmpty) None
    else
      Some(map.tail.foldLeft(map.head) { case (acc, (key, value)) =>
        if (value < acc._2) (key, value) else acc
      })
  }

  def minReduce(map: Map[String, Int]): Option[(String, Int)] = {
    if (map.isEmpty)
      None
    else
      Some(
        map.reduceLeft[(String, Int)] { case ((k1, v1), (k2, v2)) =>
          if (v2 < v1) (k2, v2) else (k1, v1)
        }
      )
  }

  def minRecursion(map: Map[String, Int]): Option[(String, Int)] = {
    @scala.annotation.tailrec
    def fold(acc: (String, Int), map: List[(String, Int)]): (String, Int) = map match {
      case Nil            => acc
      case (k, v) :: tail => fold(if (v < acc._2) (k, v) else acc, tail)
    }
    if (map.isEmpty) None else Some(fold(map.head, map.tail.toList))
  }

  // Implement scanLeft - running total, applying [f] to elements of [list] (not using scans ofc)
  def scanLeft[T](zero: T)(list: Seq[T])(f: (T, T) => T): Seq[T] = {
    @scala.annotation.tailrec
    def build(run: T, list: List[T], result: List[T]): List[T] = list match {
      case Nil          => result
      case head :: tail => build(f(run, head), tail, f(run, head) :: result)
    }
    build(zero, list.toList, List()).reverse
  }

  // Count the consistent occurences of each character in the string
  def count(s: String): List[(Char, Int)] = {
    s.foldLeft(List[(Char, Int)]())((acc, ch) =>
      acc match {
        case Nil                => (ch, 1) :: Nil
        case (_ch, cnt) :: tail => if (_ch == ch) (ch, cnt + 1) :: tail else (ch, 1) :: acc
      }
    ).reverse
  }
}
