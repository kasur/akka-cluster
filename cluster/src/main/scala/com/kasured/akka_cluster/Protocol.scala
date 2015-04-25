package com.kasured.akka_cluster

/**
 * @author kasured.
 */
object Protocol {
  sealed trait Message
  case class Prime(nth: Long) extends Message
  case class PrimeResult(value: Long) extends Message
  case object Initialize extends Message
}
