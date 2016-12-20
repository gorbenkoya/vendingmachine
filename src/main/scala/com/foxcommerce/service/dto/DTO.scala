package com.foxcommerce.service.dto

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.foxcommerce.model._
import spray.json.{DefaultJsonProtocol, JsArray, JsBoolean, JsNumber, JsObject, JsString, JsValue, PrettyPrinter, RootJsonFormat}

case class AvailableProducts(products: Map[ProductType, Int])

case class ErrorResponse(success: Boolean, error: String)

trait DTOJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val errorResponseFormat = jsonFormat2(ErrorResponse)

  implicit object AvailableProductsJsonFormat extends RootJsonFormat[AvailableProducts] {
    def write(ap: AvailableProducts) = JsObject(
      "success" -> JsBoolean(true),
      "products" -> JsArray(
        ap.products.map { case (product, price) =>
          JsObject(
            "name" -> JsString(product.getName),
            "price" -> JsNumber(price)
          )
        }.toVector
      )
    )

    def read(value: JsValue) = ???
  }

}


