enablePlugins(AkkaAppPackaging)

name := "akka-cluster"


version := "1.0"

scalaVersion := "2.11.6"


mainClass in Compile := Some("com.kasured.akka_cluster.Bootstrap")

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Typesafe Repository Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"

libraryDependencies ++= Seq(
"com.typesafe.akka" %% "akka-kernel" % "2.4-SNAPSHOT",
"com.typesafe.akka" %% "akka-actor" % "2.4-SNAPSHOT",
"com.typesafe.akka" %% "akka-cluster" % "2.4-SNAPSHOT",
"com.typesafe.akka" %% "akka-slf4j" % "2.4-SNAPSHOT",

"ch.qos.logback" % "logback-classic" % "1.1.2"
)

scalacOptions ++= Seq("-feature", "-deprecation")