package com.kasured.http_service

import akka.actor.ActorRef
import akka.contrib.pattern.ClusterClient
import akka.http.model.HttpResponse
import akka.util.Timeout

import scala.concurrent.{Await, Future, ExecutionContext}

import akka.http.server.Directives._

/**
 * @author kasured.
 */

trait Service {

  private lazy val log = org.slf4j.LoggerFactory.getLogger(this.getClass.getName)

  implicit val clusterClient: ActorRef
  implicit val executor: ExecutionContext

  val routes = {
    pathPrefix("v1" / "echo" / Rest ) { resp =>
      get {
        complete {
          Future {
            HttpResponse(entity = s"Echoing back with $resp \n")
          }
        }
      }
    } ~
    pathPrefix("v1" / "nthPrimeFor" / LongNumber ) { nth =>
      get {
        complete {
          Future {

            log.info(s"Trying to send message to cluster with $clusterClient")
            import scala.concurrent.duration._
            import scala.language.postfixOps
            import akka.pattern.ask
            implicit val timeout = Timeout(1000 seconds)

            val value = Await.result({
              (clusterClient ? ClusterClient.Send(
                "/user/dispatcherGateway", nth, localAffinity = true
              )).mapTo[Long]
            }, Duration.Inf)

            log.info(s"Result from remote cluster ${nth}th prime is $value")
            HttpResponse(entity = s"${value.toString}\n")

          }
        }
      }
    }
  }

}
