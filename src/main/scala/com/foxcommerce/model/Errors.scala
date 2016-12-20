package com.foxcommerce.model

case class NotEnoughCoinsException(message: String, insertedCoins: Int, price: Int) extends Exception

case class ProductRunOutException(message: String, product: ProductType) extends Exception

case class NoSuchProductException(message: String) extends Exception
