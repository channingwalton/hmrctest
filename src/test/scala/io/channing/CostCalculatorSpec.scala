package io.channing

import org.scalatest.{FreeSpec, MustMatchers}

class CostCalculatorSpec extends FreeSpec with MustMatchers {

  "Calculate cost from list" in {
    val input = List("Apple", "Apple", "Orange", "Apple")
    val costs = Map("Apple" -> 0.60, "Orange" -> 0.25)

    CostCalculator.costs(input, costs) mustEqual Right(2.05)
  }

  "Calculate cost when item is missing from prices list" in {
    val input = List("Apple", "Apple", "Orange", "Apple")
    val costs = Map("Orange" -> 0.25)

    CostCalculator.costs(input, costs) mustEqual Left("The following items have no price: Apple")
  }

}
