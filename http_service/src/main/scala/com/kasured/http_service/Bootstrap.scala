package com.kasured.http_service

import akka.actor.{ActorRef, ActorSystem}
import akka.contrib.pattern.ClusterClient
import akka.http.Http
import akka.http.model.HttpResponse
import akka.http.server.Directives._
import akka.stream.ActorFlowMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.concurrent.{ExecutionContext, Future, Await}
import scala.io.Source


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
            import akka.pattern.ask

            import scala.concurrent.duration._
            import scala.language.postfixOps
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

  val remoteClusterConfig = ConfigFactory.load("cluster-client")
  val clusterClientConfig = remoteClusterConfig getConfig "application.cluster"
  val remoteClusterName = clusterClientConfig getString "name"
  val remoteClusterContactPointsFile = clusterClientConfig getString "contact-points-file"

  val clusterClientSystem = ActorSystem("ClusterClientSystem", remoteClusterConfig)

  // Seed file was specified, read it
  log info s"remote cluster contact points from file: $remoteClusterContactPointsFile"
  val remoteClusterContactPoints = Source.fromFile(remoteClusterContactPointsFile).getLines().map { address =>
    clusterClientSystem actorSelection s"akka.tcp://$remoteClusterName@$address/user/receptionist"
  }.toSet
  log.info(s"Remote cluster contact points are $remoteClusterContactPoints")

  implicit val clusterClient = clusterClientSystem.actorOf(ClusterClient.props(remoteClusterContactPoints), "cluster-client")
  //clusterClient ! ClusterClient.Send("/user/dispatcherGateway", 10001, localAffinity = true)



}
