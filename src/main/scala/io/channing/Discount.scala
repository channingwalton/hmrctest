package io.channing

final case class Saving(discount: Discount, items: List[String], saving: Int)

sealed trait Discount extends Product with Serializable {
  def saving(items: List[String], prices: Map[String, Int]): Option[Saving]
}

final case class NForMDiscount(item: String, n: Int, m: Int) extends Discount {
  override def saving(items: List[String], prices: Map[String, Int]): Option[Saving] = {
    val count = items.count(_ == item)
    val discountable = (count / n) * n
    if (discountable == 0)
      None
    else {
      val normalPrice = discountable * prices(item)
      val discountPrice = normalPrice * m / n
      val saving = normalPrice - discountPrice
      Some(Saving(this, List.fill(discountable)(item), saving))
    }
  }
}