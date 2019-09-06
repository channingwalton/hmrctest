package io.channing

final case class Saving(discount: Discount, items: List[String], originalCost: Int, saving: Int)
