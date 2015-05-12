package com.kasured.http_service

import akka.actor.ActorSystem
import akka.contrib.pattern.ClusterClient
import akka.http.Http
import akka.stream.ActorFlowMaterializer
import com.typesafe.config.ConfigFactory

import scala.io.Source

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
