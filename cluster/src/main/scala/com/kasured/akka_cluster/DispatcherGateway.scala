package com.kasured.akka_cluster

import akka.actor.{Actor, Props}
import akka.pattern.PipeToSupport
import com.kasured.akka_cluster.Protocol.{Prime, PrimeResultWrapper, PrimeWrapper}

/**
 * @author kasured.
 */
class DispatcherGateway extends Actor with PipeToSupport {

  lazy val log = org.slf4j.LoggerFactory.getLogger(this.getClass.getName)

  val worker = context actorOf(Props[Worker], name = "worker")

  override def receive: Receive = {

    case request: Long =>
      log.info(s"Receive the request $request from ${sender()} and dispatching work to the worker $worker")
      worker ! PrimeWrapper(Prime(request), sender())

    case wrappedResult @ PrimeResultWrapper(result, source) =>
      log.info(s"Sending result ${result.value} to requester $source")
      source ! result.value

  }

}
