package com.kasured.akka_cluster

import akka.actor.ActorRef

/**
 * @author kasured.
 */
object Protocol {
  sealed trait Message
  case class Prime(nth: Long) extends Message
  case class PrimeWrapper(prime: Prime, source: ActorRef) extends Message
  case class PrimeResult(value: Long) extends Message
  case class PrimeResultWrapper(primeResult: PrimeResult, source: ActorRef) extends Message
  case object Initialize extends Message
}
