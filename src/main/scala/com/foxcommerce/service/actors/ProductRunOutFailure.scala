package com.foxcommerce.service.actors

import akka.actor.ActorRef
import com.foxcommerce.model.Selection

case class ProductRunOutFailure(customer: ActorRef,
                           pendingOrder: Selection,
                           insertedCoins: Int) extends Exception
