package io.channing

sealed trait Discount extends Product with Serializable {
  def saving(items: List[String], prices: Map[String, Int]): Option[Saving]
}

final case class NForMDiscount(item: String, n: Int, m: Int) extends Discount {
  override def saving(items: List[String], prices: Map[String, Int]): Option[Saving] =
    if (items.count(_ == item) < n)
      None
    else {
      val normalPrice = n * prices(item)
      val discountPrice = normalPrice * m / n
      val saving = normalPrice - discountPrice
      Some(Saving(this, List.fill(n)(item), saving))
    }
}