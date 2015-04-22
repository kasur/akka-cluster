import sbt._


object Dependencies {

  import Versions._


  val reolutiionRepos = Seq(
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "Typesafe Repository Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
  )

  val akka_actor = "com.typesafe.akka" %% "akka-actor" % Akka
  val akka_cluster = "com.typesafe.akka" %% "akka-cluster" % Akka
  val akka_slf4j = "com.typesafe.akka" %% "akka-slf4j" % Akka

  /*enable it later to play with kamon and metrics collector*/
  val io_kamon = "io.kamon" % "sigar-loader" % Kamon

  val logback = "ch.qos.logback" % "logback-classic" % Logback

}