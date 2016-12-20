package com.foxcommerce

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import com.foxcommerce.model._
import com.foxcommerce.service.VendingMachineService
import com.foxcommerce.service.dto.{DTOJsonSupport, ErrorResponse}
import com.foxcommerce.util.Config
import org.slf4j.LoggerFactory
import spray.json._

object Root extends App with Config with JsonSupport with DTOJsonSupport {

  lazy val logger = LoggerFactory.getLogger(getClass.getName)
  implicit val system = ActorSystem("api-actor-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val rejectionHandler =
    RejectionHandler.newBuilder()
      .handle {
        case MalformedRequestContentRejection(msg, cause) =>
          val msg = cause match {
            case e: NoSuchProductException => e.message
            case ex: Exception => ex.getMessage
          }
          complete(HttpResponse(StatusCodes.BadRequest,
            entity = HttpEntity(ContentTypes.`application/json`,
              ErrorResponse(success = false, msg).toJson)))
      }.result()

  val exceptionHandler =
    ExceptionHandler {
      case e: Exception =>
        import spray.json._
        logger.error(s"Server error: ${e.getMessage}")
        complete(HttpResponse(StatusCodes.InternalServerError,
          entity = HttpEntity(ContentTypes.`application/json`,
            ErrorResponse(success = false, e.getMessage).toJson)))
    }


  val route =
    handleExceptions(exceptionHandler) {
      handleRejections(rejectionHandler) {
        pathPrefix("vm") {
          get {
            path("products") {
              complete(VendingMachineService.getAvailableProducts)
            }
          } ~
            post {
              path("insertcoin") {
                entity(as[CoinsInserted]) {
                  coins =>
                    val future = VendingMachineService.insertCoins(coins)
                    complete(future)
                }
              } ~
                path("purchase") {
                  entity(as[Selection]) {
                    product =>
                      val future = VendingMachineService.purchaseProduct(product)
                      onSuccess(future) {
                        case pp: PurchasedProduct => complete(pp)
                        case noc: NotEnoughCoins => complete(noc)
                      }
                  }
                }
            }
        }
      }
    }

  val bindingFuture = Http().bindAndHandle(route, httpHost, httpPort)
  logger.debug(s"Vending machine API has started on port $httpPort")
}
