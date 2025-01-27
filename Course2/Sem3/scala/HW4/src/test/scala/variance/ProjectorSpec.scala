package variance

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ProjectorSpec extends AnyFlatSpec with Matchers {

  val wl_slides = new HelloSlide[WordLine](
    List(
      new WordLine("abc"),
      new WordLine("def"),
      new WordLine("ghi")
    )
  )
  val rwl_slides = new HelloSlide[RedactedWordLine](
    List(
      new RedactedWordLine(0.0, "abc"),
      new RedactedWordLine(1.0, "def"),
      new RedactedWordLine(0.0, "ghi")
    )
  )
  val expected1 = "abc\ndef\nghi\n"
  val expected2 = "abc\n███\nghi\n"

  "Projector[WordLine]" should "allow Converter[WordLine] but not Converter[RedactedWordLine]" in {
    new Projector[WordLine](LineConverter)
    "new Projector[WordLine](RedactedLineConverter)" shouldNot typeCheck
  }

  it should "allow Slide[WordLine] and Slide[RedactedWordLine]" in {
    val projector = new Projector[WordLine](LineConverter)
    projector.project(wl_slides) shouldEqual expected1
    projector.project(rwl_slides) shouldEqual expected1
  }

  "Projector[RedactedWordLine]" should "allow Converter[WordLine] and Converter[RedactedWordLine]" in {
    new Projector[RedactedWordLine](RedactedLineConverter)
    new Projector[RedactedWordLine](LineConverter)
  }

  it should "allow Slide[RedactedWordLine] but not Slide[WordLine]" in {
    val projector1 = new Projector[RedactedWordLine](LineConverter)
    val projector2 = new Projector[RedactedWordLine](RedactedLineConverter)
    projector1.project(rwl_slides) shouldEqual expected1
    projector2.project(rwl_slides) shouldEqual expected2
    "projector1.project(wl_slides)" shouldNot typeCheck
    "projector2.project(wl_slides)" shouldNot typeCheck
  }

}
