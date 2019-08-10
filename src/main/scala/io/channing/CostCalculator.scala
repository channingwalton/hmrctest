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
      val biggest = discounts.flatMap(_.saving(current, prices)).sortBy(_.saving).reverse.headOption
      biggest.fold(List.empty[Saving])(saving => saving :: computeSavings(current diff saving.items))
    }

    priceWithoutDiscount(input, prices) - computeSavings(input).foldLeft(0)(_ + _.saving)
  }

  private def priceWithoutDiscount(input: List[String], prices: Map[String, Int]): Int =
    input.foldLeft(0) { case (total, item) => total + prices(item) }
}
