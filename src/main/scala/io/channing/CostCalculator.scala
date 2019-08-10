package io.channing

object CostCalculator {
  def costs(input: List[String], prices: Map[String, Double]): Double =
    input.foldLeft(0.0){ case (total, item) => total + prices(item) }
}
