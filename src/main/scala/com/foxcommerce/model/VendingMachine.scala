package com.foxcommerce.model

class VendingMachine(initProductAmount: Map[ProductType, Int], initProductPrices: Map[ProductType, Int]) {

  private var earnedCoinsTotal: Int = 0
  private var insertedUserCoins: Int = 0
  private val productAmount = scala.collection.mutable.Map[ProductType, Int]() ++ initProductAmount
  private val productPrices = scala.collection.mutable.Map[ProductType, Int]() ++ initProductPrices

  def buyProduct(product: ProductType) = {
    val amount = getProductAmount(product)
    val price = getProductPrice(product)
    if (amount <= 0 || price == 0)
      throw new ProductRunOutException("Product run out error", product)

    insertedUserCoins >= price match {
      case true =>
        insertedUserCoins -= price
        earnedCoinsTotal += price
        productAmount.put(product, amount - 1)
      case false =>
        throw new NotEnoughCoinsException("Not enough coins inserted", insertedUserCoins, price)
    }
  }

  def insertCoins(coins: Int) = {
    insertedUserCoins += coins
  }

  def getEarnedCoinsTotal: Int = earnedCoinsTotal

  def getInsertedUserCoins: Int = insertedUserCoins

  private def getProductAmount(productType: ProductType): Int = productAmount.getOrElse(productType, 0)

  private def getProductPrice(productType: ProductType): Int = productPrices.getOrElse(productType, 0)


}
