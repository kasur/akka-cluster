package com.kasured.akka_cluster

import java.util.concurrent.TimeUnit

import akka.actor.{Status, Stash, Actor}
import akka.cluster.Cluster
import akka.pattern.PipeToSupport
import com.kasured.akka_cluster.Protocol.{PrimeResult, Initialize, Prime}
import com.kasured.akka_cluster.Worker.Continue

import scala.concurrent.Future
import scala.util.Random

/**
 * @author kasured.
 */

object Worker {

  sealed trait InternalMessage
  case object Continue extends InternalMessage

  def nthPrime(n: Long): Long = {
    @annotation.tailrec
    def step(nn: Long, candidate: Long): Long = isPrime(candidate) match {
      case true if nn == n => candidate
      case true => step(nn + 1, candidate + 1)
      case false => step(nn, candidate + 1)
    }
    def isPrime(candidate: Long): Boolean = candidate match {
      case x if x == 2 || x == 3 || x == 5 => true
      case x if (x & 1) == 0 || (x % 3) == 0 => false
      case _ => check(5, math.round(math.sqrt(candidate.toDouble)), candidate)
    }
    @annotation.tailrec
    def check(_n: Long, limit: Long, candidate: Long): Boolean = {
      if(_n > limit) true else {
        if(candidate % _n == 0 || candidate % (_n + 2) == 0) false else check(_n + 6, limit, candidate)
      }
    }
    step(1, 2)
  }
}

class Worker extends Actor with Stash with PipeToSupport {

  import context._

  lazy val log = org.slf4j.LoggerFactory.getLogger(this.getClass.getName)

  val cluster = Cluster(context.system)

  self ! Initialize

  def receive: Receive = uninitialized

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    log.info("[Worker stopped]")
  }

  def uninitialized: Receive = {

    case Initialize =>
      log.info(s"Starting actor initialization $self")
      Future { TimeUnit.MILLISECONDS.sleep(Random.nextInt(2000)) } map {_ => Continue} pipeTo self

    case Continue =>
      log.info(s"Becoming initialized $self")
      unstashAll()
      become(initialized)

    case Status.Failure(e) => throw new IllegalStateException("Cannot init actor", e)

    case msg =>
      log.info(s"$self not yet ready to accept message $msg from ${sender()}")
      stash()

  }

  def initialized: Receive = {
    case Prime(nth) =>
      log.info(s"[$self started calculating prime for sequence number $nth]")
      sender() ! PrimeResult(Worker.nthPrime(nth))
      log.info(s"[$self finished calculating prime for sequence number $nth]")
  }
}
