
name := "akka-cluster"

version := "1.0"

scalaVersion := "2.11.6"

scalacOptions in Compile ++= Seq("-encoding", "UTF-8", "-target:jvm-1.7",
  "-deprecation", "-feature", "-unchecked",
  "-Xlog-reflective-calls",
  "-Xlint"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4-SNAPSHOT",
  "com.typesafe.akka" %% "akka-cluster" % "2.4-SNAPSHOT",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4-SNAPSHOT",

  /*enable it later to play with kamon and metrics collector*/
  /*"io.kamon" % "sigar-loader" % "1.6.5-rev001",*/

  "ch.qos.logback" % "logback-classic" % "1.1.2"
)

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Typesafe Repository Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
)

javaOptions in run ++= Seq(
  "-Xms128m", "-Xmx1024m"
)

Keys.fork in run := true

mainClass in (Compile, run) := Some("com.kasured.akka_cluster.Bootstrap")
