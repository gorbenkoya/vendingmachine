package com.foxcommerce.service.actors

import akka.actor.SupervisorStrategy.{Escalate, Restart}
import akka.actor.{Actor, OneForOneStrategy, Props}
import org.slf4j.LoggerFactory
import scala.concurrent.duration._


class VendingMachineSupervisor extends Actor {

  lazy val logger = LoggerFactory.getLogger(getClass.getName)

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1.minute) {
      case e: ProductRunOutFailure =>
        logger.error(s"Machine failed on run out of products error. Trying to recover...")
        Restart
      case _: Exception =>
        Escalate
    }

  val machine = context.actorOf(Props[VendingMachineActor], name = "vendingMachine")

  def receive = {
    case request => machine.forward(request)
  }

}
