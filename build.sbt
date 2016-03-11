name := """websocket-play"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
//  "com.typesafe.akka" %% "akka-remote" % "2.4.2",
  "com.typesafe.akka" %% "akka-cluster" % "2.4.2",
  "com.typesafe.akka" %% "akka-cluster-tools" % "2.4.2",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.2",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0-RC1" % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"


fork in run := true