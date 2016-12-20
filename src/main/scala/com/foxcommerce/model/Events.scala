package com.foxcommerce.model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{JsNumber, _}

case class CoinsInserted(amount: Int)

case class CoinsReceived(amount: Int, total: Int)

case class Selection(product: ProductType)

case class PurchasedProduct(product: ProductType)

case class NotEnoughCoins(insertedAmount: Int, price: Int)


trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val printer = PrettyPrinter

  implicit object CoinsReceivedJsonFormat extends RootJsonFormat[CoinsReceived] {
    def write(cr: CoinsReceived) = JsObject(
      "success" -> JsBoolean(true),
      "inserted" -> JsNumber(cr.amount),
      "total" -> JsNumber(cr.total)
    )

    def read(value: JsValue) = ???
  }

  implicit object CoinsInsertedJsonFormat extends RootJsonFormat[CoinsInserted] {
    def read(value: JsValue) = {
      value.asJsObject.getFields("amount") match {
        case Seq(JsNumber(amount)) => CoinsInserted(amount.toInt)
        case _ => throw DeserializationException("amount field is missing or wrong")
      }
    }

    def write(ci: CoinsInserted) = defaultWrite
  }

  implicit object SelectionJsonFormat extends RootJsonFormat[Selection] {
    def read(value: JsValue) = {
      value.asJsObject.getFields("product") match {
        case Seq(JsString(product)) => Selection(DefaultProductsHelper.getProductByName(product))
        case _ => throw DeserializationException("product field is missing")
      }
    }

    def write(s: Selection) = defaultWrite
  }

  implicit object PurchaseJsonFormat extends RootJsonFormat[PurchasedProduct] {
    def read(value: JsValue) = PurchasedProduct(Bounty) //stub, not used

    def write(pp: PurchasedProduct) = JsObject(
      "success" -> JsBoolean(true),
      "product" -> JsString(pp.product.getName)
    )
  }

  implicit object NotEnoughCoinsJsonFormat extends RootJsonFormat[NotEnoughCoins] {
    def read(value: JsValue) = ???

    def write(nec: NotEnoughCoins) = JsObject(
      "success" -> JsBoolean(false),
      "error" -> JsString("not enough coins"),
      "coinsInserted" -> JsNumber(nec.insertedAmount),
      "price" -> JsNumber(nec.price)
    )
  }

  def defaultWrite = JsObject("success" -> JsBoolean(true))

}
