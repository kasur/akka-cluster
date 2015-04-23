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

  val akka_http_core = "com.typesafe.akka" %% "akka-http-core-experimental" % Akka_Http
  val akka_http = "com.typesafe.akka" %% "akka-http-experimental" % Akka_Http

  val akka_stream = "com.typesafe.akka" %% "akka-stream-experimental" % Akka_Stream

  val common_deps = Seq(akka_slf4j, logback)
  val cluster_deps = common_deps ++ Seq(akka_cluster, akka_actor)
  val akka_http_service_deps = common_deps ++ Seq(akka_actor, akka_http_core, akka_http, akka_stream)

}