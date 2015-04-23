package com.kasured.http_service

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.model.HttpResponse
import akka.http.server.Directives._
import akka.stream.ActorFlowMaterializer
import com.typesafe.config.ConfigFactory

/**
 * @author kasured.
 */

trait Service {

  val routes = {
    pathPrefix("v1" / "echo" / Rest ) { resp =>
      get {
        complete { HttpResponse(entity = s"Echoing back with $resp \n") }
      }
    }
  }

}

object Bootstrap extends App with Service {

  lazy val log = org.slf4j.LoggerFactory.getLogger(this.getClass.getName)

  val config = ConfigFactory.load("http-service")
  val appConfig = config getConfig "application"

  implicit val system = ActorSystem("akka-http-as",config)
  implicit val materializer = ActorFlowMaterializer()
  implicit val executor = system.dispatcher

  val hostname = appConfig getString "bind-hostname"
  val port = appConfig getInt "bind-port"

  log.debug("Actor system is {}", system)
  log.debug("[Binding http service to {}:{}]", hostname, port)

  Http(system).bindAndHandle(routes, interface = hostname, port = port)

}
