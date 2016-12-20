package com.foxcommerce.model

case object Mars extends ProductType

case object Snickers extends ProductType

case object Bounty extends ProductType

object DefaultProductsHelper {

  final val defaultProducts = Map[ProductType, Int](
    Bounty -> 10,
    Mars -> 3,
    Snickers -> 10
  )

  final val defaultPrices = Map[ProductType, Int](
    Bounty -> 2,
    Mars -> 1,
    Snickers -> 3
  )

  def getProductByName(name: String): ProductType = {
    name.toLowerCase match {
      case "mars" => Mars
      case "snickers" => Snickers
      case "bounty" => Bounty
      case _ => throw NoSuchProductException(s"No such product $name")
    }
  }

}

