package com.kasured.akka_cluster

import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._

/**
 * @author kasured.
 */
class ClusterListener extends Actor {

  lazy val log = org.slf4j.LoggerFactory.getLogger(this.getClass.getName)

  val cluster = Cluster(context.system)

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    //subscribing to cluster events
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }


  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }

  override def receive: Receive = {

    case MemberUp(member) =>
      log.info("[Member is up: {}]", member.address)
    case MemberRemoved(member, previousState) =>
      log.info("[Member has been removed: {} after {}]", member.address, previousState, Option.empty)
    case UnreachableMember(member) =>
      log.info("[Member became unreachable: {}]", member.address)
    case _: MemberEvent => // ignoreMe
  }
}
