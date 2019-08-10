package io.channing

object CostCalculator {
  def costs(input: List[String], prices: Map[String, Double]): Either[String, Double] = {
    val itemsMissingPrice = input.filterNot(prices.keySet).toSet
    if (itemsMissingPrice.nonEmpty)
      Left(s"The following items have no price: ${itemsMissingPrice.mkString(", ")}")
    else
      Right(input.foldLeft(0.0){ case (total, item) => total + prices(item) })
  }
}
