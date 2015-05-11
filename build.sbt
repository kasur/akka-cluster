import sbt.Keys._

import Dependencies._

lazy val commonSettings = Seq(

  version := "1.0.0",

  scalaVersion := "2.11.6",

  scalacOptions in Compile ++= Seq("-encoding", "UTF-8", "-target:jvm-1.7",
    "-deprecation", "-feature", "-unchecked",
    "-Xlog-reflective-calls",
    "-Xlint"
  ),

  javaOptions in run ++= Seq(
    "-Xms128m", "-Xmx1024m",
    "-XX:PrintGCDetails", "-XX:PrintGCDateStamps", "-Xloggc:akka-cluster-log.gc"
  ),

  Keys.fork in run := true,

  resolvers ++= Dependencies.reolutiionRepos

)

lazy val root = project.in(file("."))
  .aggregate(cluster, http_service)
  .settings(commonSettings: _*)

lazy val cluster = project.in(file("cluster"))
  .enablePlugins(JavaAppPackaging)
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= cluster_deps)
  .settings(mainClass in Compile := Some("com.kasured.akka_cluster.Bootstrap"))

lazy val http_service = project.in(file("http_service"))
  .enablePlugins(JavaAppPackaging)
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= akka_http_service_deps )
  .settings(mainClass in Compile := Some("com.kasured.http_service.Bootstrap"))
