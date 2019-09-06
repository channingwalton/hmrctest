package io.channing

import org.scalatest.{FreeSpec, MustMatchers}

class CostCalculatorSpec extends FreeSpec with MustMatchers {

  private val apple = 60
  private val orange = 25
  private val banana = 20
  val costs = Map("Apple" -> apple, "Orange" -> orange, "Banana" -> banana)

  "Calculate cost from list" in {
    val input = List("Apple", "Apple", "Orange", "Apple")

    CostCalculator.costs(input, costs) mustEqual Right(3 * apple + orange)
  }

  "Calculate cost when item is missing from prices list" in {
    val input = List("Apple", "Apple", "Orange", "Apple")
    val costs = Map("Orange" -> orange)

    CostCalculator.costs(input, costs) mustEqual Left("The following items have no price: Apple")
  }

  "Discounts applied" - {
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

    "discounts apply multiple times" in {
      val input = List("Apple", "Apple", "Apple", "Apple", "Apple", "Orange", "Orange", "Orange", "Orange", "Orange", "Orange", "Orange", "Orange")
      CostCalculator.costs(input, costs, discounts) mustEqual Right(3 * apple + 6 * orange)
    }
  }

  "biggest discounts applied first" in {
    val discounts: List[Discount] = List(NForMDiscount("Apple", 4, 1), NForMDiscount("Apple", 3, 2))
    val input = List.fill(7)("Apple")
    CostCalculator.costs(input, costs, discounts) mustEqual Right(3 * apple)
  }

  "1 banana" in {
    val discounts: List[Discount] = List(NForMDiscount("Banana", 2, 1))
    val input = List("Banana")
    CostCalculator.costs(input, costs, discounts) mustEqual Right(1 * banana)
  }

  "2 banana" in {
    val discounts: List[Discount] = List(NForMDiscount("Banana", 2, 1))
    val input = List("Banana", "Banana")
    CostCalculator.costs(input, costs, discounts) mustEqual Right(1 * banana)
  }

  "3 banana" in {
    val discounts: List[Discount] = List(NForMDiscount("Banana", 2, 1))
    val input = List("Banana", "Banana", "Banana")
    CostCalculator.costs(input, costs, discounts) mustEqual Right(2 * banana)
  }

  "apples and bananas" in {
    val discounts: List[Discount] = List(NForMDiscount("Banana", 2, 1), NForMDiscount("Apple", 2, 1))
    val input = List("Apple", "Apple", "Banana", "Banana")
    CostCalculator.costs(input, costs, discounts) mustEqual Right(apple)
  }

  "banana and apple combination" in {
    val discounts: List[Discount] = Nil
    val input = List("Apple", "Apple", "Banana", "Banana")
    CostCalculator.costs(input, costs, discounts) mustEqual Right(2 * apple + 2 * banana)
  }
}
