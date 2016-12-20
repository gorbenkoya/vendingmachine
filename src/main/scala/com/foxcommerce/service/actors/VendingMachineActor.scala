package com.foxcommerce.service.actors

import akka.actor.Actor
import com.foxcommerce.model._
import org.slf4j.LoggerFactory

class VendingMachineActor extends Actor {

  private val logger = LoggerFactory.getLogger(getClass.getName)

  var machine = new VendingMachine(DefaultProductsHelper.defaultProducts, DefaultProductsHelper.defaultPrices)

  def receive = {
    case CoinsInserted(coins) =>
      machine.insertCoins(coins)
      logger.debug(s"Coins inserted: $coins, customer total: ${machine.getInsertedUserCoins}, total in VM: ${machine.getEarnedCoinsTotal}")
      sender.tell(CoinsReceived(coins, machine.getInsertedUserCoins), self)
    case selection@Selection(productType) =>
      try {
        machine.buyProduct(productType)
        logger.debug(s"Product '$productType' is purchased successfully. Total coins earned by VM: ${machine.getEarnedCoinsTotal}")
        sender.tell(PurchasedProduct(productType), self)
      } catch {
        case e: NotEnoughCoinsException =>
          logger.warn(s"Not enough coins inserted - product: $productType, coinsInserted: ${e.insertedCoins}, price: ${e.price}")
          sender.tell(NotEnoughCoins(e.insertedCoins, e.price), self)
        case e: ProductRunOutException =>
          logger.error("Product run out error, machine requires service")
          throw ProductRunOutFailure(sender, selection, machine.getInsertedUserCoins)
      }
  }

  override def postRestart(failure: Throwable): Unit = {
    logger.warn(s"Restarting vending machine after service...")
    failure match {
      case ProductRunOutFailure(customer, pendingOrder, insertedCoins) =>
        machine = new VendingMachine(DefaultProductsHelper.defaultProducts, DefaultProductsHelper.defaultPrices)
        machine.insertCoins(insertedCoins)
        logger.warn(s"Re-submitting pending order $pendingOrder, coins inserted $insertedCoins")
        context.self.tell(pendingOrder, customer)
    }
  }

}
