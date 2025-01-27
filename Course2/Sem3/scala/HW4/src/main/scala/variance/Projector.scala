package variance

import scala.util.Random

trait Converter[-S] {
  def convert(value: S): String
}

trait Slide[+R] {
  def read: (Option[R], Slide[R])
}

class Projector[R](converter: Converter[R]) {
  def project(screen: Slide[R]): String = {
    @scala.annotation.tailrec
    def fold(slide: Slide[R], acc: String): String = {
      slide.read match {
        case (Some(value), next) => fold(next, acc + converter.convert(value))
        case (None, _)           => acc
      }
    }
    fold(screen, "")
  }
}

class WordLine(val word: String)

class RedactedWordLine(val redactionFactor: Double, word: String) extends WordLine(word)

object LineConverter extends Converter[WordLine] {
  override def convert(value: WordLine): String = value.word + "\n"
}

object RedactedLineConverter extends Converter[RedactedWordLine] {
  override def convert(value: RedactedWordLine): String = Random.nextDouble() match {
    case p if p < value.redactionFactor => "█" * value.word.length + "\n"
    case _                              => value.word + "\n"
  }
}

class HelloSlide[R <: WordLine](lines: Seq[R]) extends Slide[R] {
  override def read: (Option[R], HelloSlide[R]) = lines match {
    case head +: tail => (Some(head), new HelloSlide(tail))
    case _            => (None, this)
  }
}
