package tree

import scala.annotation.tailrec
import scala.util.control.TailCalls.{done, TailRec}

sealed trait Error
case object EmptyTreeError extends Error

sealed trait Tree {
  self =>
}
case object Empty extends Tree
case class Node(value: Int, left: Tree = Empty, right: Tree = Empty) extends Tree {
  self =>

  def add(x: Int): Node = {
    @tailrec
    def insert(cur: Node, path: List[Node], isl: Boolean): Node = {
      cur match {
        case Node(value, Empty, right) if x < value =>
          val node = Node(value, Node(x), right)
          rebuildTree(path, node, isl)
        case Node(value, left, Empty) if x >= value =>
          val node = Node(value, left, Node(x))
          rebuildTree(path, node, isl)
        case Node(value, left: Node, _) if x < value =>
          insert(left, cur :: path, isl = true)
        case Node(value, _, right: Node) if x >= value =>
          insert(right, cur :: path, isl = false)
        case _ => rebuildTree(path, cur, isl)
      }
    }
    @tailrec
    def rebuildTree(path: List[Node], cur: Node, isl: Boolean): Node = {
      path match {
        case Nil => cur
        case par :: tail =>
          val _par = if (isl) par.copy(left = cur) else par.copy(right = cur)
          rebuildTree(tail, _par, isl = tail.nonEmpty && tail.headOption.exists(_.left == par))
      }
    }
    insert(self, Nil, isl = true)
  }

  // trampoline optimized
  def delete(value: Int): Either[Error, Node] = {
    @tailrec
    def findMin(node: Node): Int = node match {
      case Node(value, Empty, _)  => value
      case Node(_, left: Node, _) => findMin(left)
    }
    def dlt(node: Node, x: Int): TailRec[Tree] = node match {
      case Node(value, left: Node, right) if x < value => done(Node(value, dlt(left, x).result, right))
      case Node(value, left, right: Node) if x > value => done(Node(value, left, dlt(right, x).result))
      case Node(value, left, right) if value == x =>
        (left, right) match {
          case (Empty, Empty) => done(Empty)
          case (le, Empty)    => done(le)
          case (Empty, ri)    => done(ri)
          case (lft: Node, rgt: Node) =>
            val minValue = findMin(rgt)
            done(Node(minValue, lft, dlt(rgt, minValue).result))
        }
      case nd: Node => done(nd)
    }
    dlt(self, value).result match {
      case Empty    => Left(EmptyTreeError)
      case nd: Node => Right(nd)
    }
  }

  def foldLeft[A](z: A)(f: (A, Int) => A)(order: (Node, List[Node]) => List[Node]): A = {
    @tailrec
    def fold(stk: List[Node], acc: A): A = stk match {
      case Nil => acc
      case Node(value, left, right) :: tail =>
        val _stk = order(Node(value, left, right), tail)
        fold(_stk, f(acc, value))
    }
    fold(List(self), z)
  }

  def depthFirstSearch[A](z: A)(f: (A, Int) => A): A = {
    foldLeft(z)(f) { case (Node(_, left, right), tail) =>
      right match {
        case Empty =>
          left match {
            case Empty      => tail
            case node: Node => node :: tail
          }
        case node: Node =>
          left match {
            case Empty     => node :: tail
            case lft: Node => lft :: node :: tail
          }
      }
    }
  }

  def breadthFirstSearch[A](z: A)(f: (A, Int) => A): A = {
    foldLeft(z)(f) { case (Node(_, left, right), tail) =>
      tail ++ List(left, right).collect { case node: Node => node }
    }
  }

  def max(search: Int => ((Int, Int) => Int) => Int): Int = search(Int.MinValue)(Math.max)

  def min(search: Int => ((Int, Int) => Int) => Int): Int = search(Int.MaxValue)(Math.min)

  def size(): Int = depthFirstSearch(0)((cnt: Int, _) => cnt + 1)

  def print(): Unit = {
    @tailrec
    def loop(cur: List[Tree], nxt: List[Tree], line: List[String]): Unit = cur match {
      case Nil if nxt.isEmpty =>
        if (line.nonEmpty) println(line.mkString(" "))
      case Nil =>
        println(line.mkString(" "))
        loop(nxt, Nil, Nil)
      case Empty :: rest =>
        loop(rest, nxt, line)
      case Node(value, left, right) :: rest =>
        loop(rest, nxt ++ List(left, right), line :+ value.toString)
    }
    loop(List(self), Nil, Nil)
  }

}
