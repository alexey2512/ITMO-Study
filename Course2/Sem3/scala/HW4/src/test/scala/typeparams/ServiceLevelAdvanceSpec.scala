package typeparams

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ServiceLevelAdvanceSpec extends AnyFlatSpec with Matchers {

  val economy = new ServiceLevelAdvance[Economy]

  "advance" should "can upgrade from Economy to all leafs" in {
    val business = economy.advance[ExtendedEconomy].advance[Business]
    business.advance[Platinum]
    business.advance[Elite]
    economy.advance[UpgradedEconomy].advance[Special1b]
  }

  it should "allow to move faster" in {
    economy.advance[Special1b]
    economy.advance[Business]
    economy.advance[Platinum]
    economy.advance[Elite]
  }

  it should "not allow movement to poverty" in {
    "new ServiceLevelAdvance[ExtendedEconomy].advance[Economy]" shouldNot typeCheck
    "new ServiceLevelAdvance[Business].advance[Economy]" shouldNot typeCheck
    "new ServiceLevelAdvance[Platinum].advance[Business]" shouldNot typeCheck
    "new ServiceLevelAdvance[Elite].advance[Business]" shouldNot typeCheck
  }

}
