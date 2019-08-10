package io.channing

import org.scalatest.{FreeSpec, MustMatchers}

class CostCalculatorSpec extends FreeSpec with MustMatchers {

  private val apple = 60
  private val orange = 25

  "Calculate cost from list" in {
    val input = List("Apple", "Apple", "Orange", "Apple")
    val costs = Map("Apple" -> apple, "Orange" -> orange)

    CostCalculator.costs(input, costs) mustEqual Right(3 * apple + orange)
  }

  "Calculate cost when item is missing from prices list" in {
    val input = List("Apple", "Apple", "Orange", "Apple")
    val costs = Map("Orange" -> orange)

    CostCalculator.costs(input, costs) mustEqual Left("The following items have no price: Apple")
  }

  "Discounts applied" - {
    val costs = Map("Apple" -> apple, "Orange" -> orange)
    val discounts: List[Discount] = List(NForMDiscount("Apple", 2, 1), NForMDiscount("Orange", 3, 2))

    "no discounts apply" in {
      val input = List("Apple", "Orange", "Orange")
      CostCalculator.costs(input, costs, discounts) mustEqual Right(apple + 2 * orange)
    }

    "apples discounts" in {
      val input = List("Apple", "Apple", "Orange", "Orange")
      CostCalculator.costs(input, costs, discounts) mustEqual Right(1 * apple + 2 * orange)
    }

    "some apples discounts" in {
      val input = List("Apple", "Apple", "Apple", "Orange", "Orange")
      CostCalculator.costs(input, costs, discounts) mustEqual Right(2 * apple + 2 * orange)
    }

    "some apples and some oranges discount" in {
      val input = List("Apple", "Apple", "Apple", "Orange", "Orange", "Orange", "Orange")
      CostCalculator.costs(input, costs, discounts) mustEqual Right(2 * apple + 3 * orange)
    }
  }

}
