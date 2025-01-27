package task1.hierarchy

import task1._

import scala.annotation.tailrec
import scala.util.control.TailCalls._

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

trait Apply[F[_]] extends Functor[F] {
  def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
}

trait Applicative[F[_]] extends Apply[F] {
  def pure[A](a: A): F[A]
}

trait FlatMap[F[_]] extends Apply[F] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  /** Несмотря на название, в этом задании необязательно реализовывать через @tailrec. Но обязательно, чтоб он был
    * стекобезопасным.
    */
  def tailRecM[A, B](a: A)(f: A => F[Either[A, B]]): F[B]
}

trait Monad[F[_]] extends FlatMap[F] with Applicative[F] {
  def pure[A](a: A): F[A]

  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}

object TypeClasses {

  implicit val treeMonad: Monad[Tree] = new Monad[Tree] {

    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = flatMap(fa)(a => pure(f(a)))

    override def ap[A, B](ff: Tree[A => B])(fa: Tree[A]): Tree[B] = flatMap(ff)(f => map(fa)(f))

    override def pure[A](a: A): Tree[A] = Leaf(a)

    override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] = {
      def build(source: Tree[A]): TailRec[Tree[B]] = source match {
        case Leaf(s) => done(f(s))
        case Branch(left, right) =>
          for {
            left_ <- tailcall(build(left))
            right_ <- tailcall(build(right))
          } yield Branch(left_, right_)
      }
      build(fa).result
    }

    // it was interesting to generate algo idea, still made it via tailrec
    override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = {

      case class Node[T](node: Tree[T], parent: Int)

      @tailrec
      def toVector(in: Int, nodes: Vector[Node[Either[A, B]]]): Vector[Node[Either[A, B]]] =
        if (in >= nodes.size) nodes
        else
          nodes(in) match {
            case Node(Branch(left, right), _) => toVector(in + 1, nodes :+ Node(left, in) :+ Node(right, in))
            case Node(Leaf(Left(value)), p)   => toVector(in, nodes.updated(in, Node(f(value), p)))
            case Node(Leaf(Right(_)), _)      => toVector(in + 1, nodes)
          }

      val nodesA: Vector[Node[Either[A, B]]] = toVector(0, Vector(Node(f(a), 0)))
      val nodesB: Vector[Tree[B]] = Vector.fill(nodesA.size)(nodesA.last.node match {
        case Leaf(Right(value)) => Leaf(value)
        case _                  => throw new IllegalStateException
      })

      @tailrec
      def toTree(in: Int, nodes: Vector[Tree[B]]): Tree[B] = in match {
        case 0 => nodes.head
        case i =>
          val _nodes: Vector[Tree[B]] = (nodesA(i - 1).node, nodesA(i).node) match {
            case (Leaf(Right(lv)), Leaf(Right(rv))) => nodes.updated(i - 1, Leaf(lv)).updated(i, Leaf(rv))
            case (Leaf(Right(lv)), Branch(_, _))    => nodes.updated(i - 1, Leaf(lv))
            case (Branch(_, _), Leaf(Right(rv)))    => nodes.updated(i, Leaf(rv))
            case (Branch(_, _), Branch(_, _))       => nodes
            case _                                  => throw new IllegalStateException
          }
          toTree(i - 2, _nodes.updated(nodesA(i).parent, Branch(_nodes(i - 1), _nodes(i))))
      }

      toTree(nodesA.size - 1, nodesB)
    }
  }

}
