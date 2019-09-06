package io.channing

object CostCalculator {

  def costs(input: List[String], prices: Map[String, Int], discounts: List[Discount] = Nil): Either[String, Int] = {
    val itemsMissingPrice = input.filterNot(prices.keySet).toSet
    if (itemsMissingPrice.nonEmpty)
      Left(s"The following items have no price: ${itemsMissingPrice.mkString(", ")}")
    else
      Right(computePrice(input, prices, discounts))
  }

  private def computePrice(input: List[String], prices: Map[String, Int], discounts: List[Discount]): Int = {
    def computeSavings(current: List[String]): List[Saving] = {
      // find the biggest discount to apply first before applying smaller discounts
      val biggest: Option[Saving] = discounts.flatMap(_.saving(current, prices)).sortBy(_.saving).reverse.headOption
      biggest.fold(List.empty[Saving])(saving => saving :: computeSavings(current diff saving.items))
    }

    val savings: List[Saving] = computeSavings(input)
    val bananaAppleSavings = filterAppleBananaCombo(savings)

    priceWithoutDiscount(input, prices) - bananaAppleSavings.foldLeft(0)(_ + _.saving)
  }

  def filterAppleBananaCombo(savings: List[Saving]): List[Saving] =
    if (savings.exists(_.items.contains("Apple")) && savings.exists(_.items.contains("Banana"))) {
      val ab = savings.filter(savings => savings.discount.appliesTo("Apple") || savings.discount.appliesTo("Banana"))
      val cheapest = ab.sortBy(_.originalCost).headOption
      val adjustedCheapest = cheapest.map(s => s.copy(saving = s.originalCost))

      val tweaked = for {
        c <- cheapest
        ac <- adjustedCheapest
      } yield ac :: savings.filterNot(_ == c)

      tweaked.getOrElse(savings)
    } else
      savings

  private def priceWithoutDiscount(input: List[String], prices: Map[String, Int]): Int =
    input.foldLeft(0) { case (total, item) => total + prices(item) }
}
