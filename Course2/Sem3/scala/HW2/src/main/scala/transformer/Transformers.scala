package transformer

object Transformers {

  val duplicate: String => String = s => s + s

  val divide: String => String = s => s.take(s.length / 2)

  val revert: String => String = s => s.reverse

  val closure: String => (String => String) => String = s => f => f(s)

}
