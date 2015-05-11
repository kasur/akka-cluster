import sbt._


object Dependencies {

  import Versions._


  val reolutiionRepos = Seq(
    "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
  )

  val akka_actor = "com.typesafe.akka" %% "akka-actor" % Akka
  val akka_cluster = "com.typesafe.akka" %% "akka-cluster" % Akka
  val akka_slf4j = "com.typesafe.akka" %% "akka-slf4j" % Akka

  /*enable it later to play with kamon and metrics collector*/
  val io_kamon = "io.kamon" % "sigar-loader" % Kamon

  val logback = "ch.qos.logback" % "logback-classic" % Logback

  val akka_http_core = "com.typesafe.akka" %% "akka-http-core-experimental" % Akka_Http
  val akka_http = "com.typesafe.akka" %% "akka-http-experimental" % Akka_Http

  val akka_stream = "com.typesafe.akka" %% "akka-stream-experimental" % Akka_Stream

  /*for ClusterClient usage scenarios*/
  val akka_contrib = "com.typesafe.akka" %% "akka-contrib" % Akka

  val common_deps = Seq(akka_slf4j, logback)
  val cluster_deps = common_deps ++ Seq(akka_cluster, akka_actor, akka_contrib)
  val akka_http_service_deps = common_deps ++ Seq(akka_actor, akka_http_core, akka_http, akka_stream, akka_contrib)

}