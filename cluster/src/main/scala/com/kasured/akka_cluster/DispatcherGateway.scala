package com.kasured.akka_cluster

import akka.actor.{Actor, Props}
import akka.pattern.PipeToSupport
import com.kasured.akka_cluster.Protocol.{Prime, PrimeResultWrapper, PrimeWrapper}

/**
 * @author kasured.
 */
class DispatcherGateway extends Actor with PipeToSupport {

  import context._

  lazy val log = org.slf4j.LoggerFactory.getLogger(this.getClass.getName)

  override def receive: Receive = {

    case request @ Prime(nth) =>

      val worker = system actorOf(Props[Worker], name = "worker")
      log.info(s"Dispatching work to the worker $worker")
      worker ! PrimeWrapper(request, sender())

    case wrappedResult @ PrimeResultWrapper(result, source) =>
      log.info(s"Sending result ${result.value} to requester $source")
      source ! result
  }

}
