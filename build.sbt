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
    "-Xms128m", "-Xmx1024m"
  ),

  Keys.fork in run := true,

  resolvers ++= Dependencies.reolutiionRepos

)

lazy val root = project.in(file("."))
  .aggregate(cluster)

lazy val cluster = project.in(file("cluster"))
  .enablePlugins(JavaAppPackaging)
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= Seq(akka_cluster, akka_actor, akka_slf4j, logback))
  .settings(mainClass in (Compile, run) := Some("com.kasured.akka_cluster.Bootstrap"))

