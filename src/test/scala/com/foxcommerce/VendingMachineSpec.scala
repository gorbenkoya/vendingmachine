package com.foxcommerce

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.foxcommerce.model._
import com.foxcommerce.service.actors.VendingMachineSupervisor
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class VendingMachineSpec() extends TestKit(ActorSystem("VendingMachineSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "Vending Machine Spec" must {

    val vendingMachine = TestActorRef[VendingMachineSupervisor]

    "purchase product" in {
      vendingMachine ! CoinsInserted(3)
      expectMsg(CoinsReceived(3, 3))
      vendingMachine ! Selection(Snickers)
      expectMsg(PurchasedProduct(Snickers))
    }

    "not enough coins" in {
      vendingMachine ! CoinsInserted(1)
      expectMsg(CoinsReceived(1, 1))
      vendingMachine ! Selection(Bounty)
      expectMsg(NotEnoughCoins(1, 2))
    }

    "no product failure and recover" in {
      vendingMachine ! CoinsInserted(4)
      expectMsg(CoinsReceived(4, 5))
      vendingMachine ! Selection(Mars)
      vendingMachine ! Selection(Mars)
      vendingMachine ! Selection(Mars)
      vendingMachine ! Selection(Mars)
      expectMsg(PurchasedProduct(Mars))
    }

  }
}


