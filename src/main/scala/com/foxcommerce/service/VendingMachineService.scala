package com.foxcommerce.service

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.foxcommerce.model._
import com.foxcommerce.service.actors.VendingMachineSupervisor
import com.foxcommerce.service.dto.AvailableProducts

import scala.concurrent.Future
import scala.concurrent.duration._

object VendingMachineService {

  implicit val timeout = Timeout(5.seconds)

  private val system = ActorSystem("vendingMachine")
  private val machine = system.actorOf(Props[VendingMachineSupervisor], "vendingMachineSupervisor")

  def getAvailableProducts: AvailableProducts = {
    AvailableProducts(DefaultProductsHelper.defaultPrices)
  }

  def insertCoins(coins: CoinsInserted): Future[CoinsReceived] = {
    (machine ? coins).mapTo[CoinsReceived]
  }

  def purchaseProduct(product: Selection): Future[Any] = {
    machine ? product
  }

}
