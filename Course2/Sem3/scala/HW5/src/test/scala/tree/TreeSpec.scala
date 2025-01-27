package tree

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TreeSpec extends AnyFlatSpec with Matchers {

  val tree1: Node = Node(0)
  val tree2: Node = Node(1, Node(2))
  val tree3: Node = Node(1, Empty, Node(0))
  val tree4: Node = Node(4, Node(2, Node(1), Node(3)), Node(6, Node(5), Node(7)))
  val tree5: Node = Node(5, Node(4, Node(3, Node(2, Node(1)))))
  val tree6: Node = Node(10, Node(5, Node(1), Node(8, Node(6))), Node(14, Node(12, Empty, Node(13))))

  def extract(response: Either[Error, Node]): Node = {
    response match {
      case Right(node) => node
      case Left(error) => fail(s"failed with error $error")
    }
  }

  "add" should "add new element to tree correctly" in {
    tree1.add(1) shouldEqual Node(0, Empty, Node(1))
    tree2.add(3) shouldEqual Node(1, Node(2), Node(3))
    tree3.add(2) shouldEqual Node(1, Empty, Node(0, Empty, Node(2)))
  }

  it should "add new element regardless its containing in tree" in {
    tree1.add(0) shouldEqual Node(0, Empty, Node(0))
    tree2.add(1) shouldEqual Node(1, Node(2), Node(1))
    tree3.add(0) shouldEqual Node(1, Node(0), Node(0))
  }

  "delete" should "delete the element if tree is correct BST" in {
    extract(tree4.delete(1)) shouldEqual Node(4, Node(2, Empty, Node(3)), Node(6, Node(5), Node(7)))
    extract(tree5.delete(2)) shouldEqual Node(5, Node(4, Node(3, Node(1))))
    extract(tree6.delete(10)) shouldEqual Node(12, Node(5, Node(1), Node(8, Node(6))), Node(14, Node(13)))
  }

  it should "do nothing when element not in tree" in {
    extract(tree1.delete(1)) shouldEqual tree1
    extract(tree3.delete(3)) shouldEqual tree3
    extract(tree5.delete(10)) shouldEqual tree5
  }

  it should "delete the highest of occurrences if there are any" in {
    extract(tree2.add(2).delete(2)) shouldEqual tree2
    extract(tree4.add(4).delete(4)) shouldEqual tree4
    extract(tree6.add(1).delete(1)) shouldEqual tree6
  }

  it should "do nothing if can not find element" in {
    extract(tree2.add(1).delete(2)) shouldEqual tree2.add(1)
  }

  it should "return error when size of tree is 1" in {
    tree1.delete(0) match {
      case Left(EmptyTreeError) => succeed
      case _                    => fail(s"expected error")
    }
  }

  "bfs" should "correctly work" in {
    tree4.breadthFirstSearch(0)(_ + _) shouldEqual 28
    tree5.breadthFirstSearch('a')((a, b) => (a + b).toChar) shouldEqual 'p'
    tree6.breadthFirstSearch(100)((a, _) => a) shouldEqual 100
  }

  "dfs" should "correctly work" in {
    tree4.depthFirstSearch(0)(_ + _) shouldEqual 28
    tree5.depthFirstSearch('a')((a, b) => (a + b).toChar) shouldEqual 'p'
    tree6.depthFirstSearch(100)((a, _) => a) shouldEqual 100
  }

  "max/max" should "find the maximum/minimum in tree" in {
    tree1.max(tree1.depthFirstSearch) shouldEqual 0
    tree2.min(tree2.depthFirstSearch) shouldEqual 1
    tree3.max(tree3.breadthFirstSearch) shouldEqual 1
    tree4.min(tree4.breadthFirstSearch) shouldEqual 1
  }

  "size" should "return the size of tree" in {
    tree1.size() shouldEqual 1
    tree2.size() shouldEqual 2
    tree5.size() shouldEqual 5
    tree6.size() shouldEqual 8
  }

}
