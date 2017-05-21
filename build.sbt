organization := "ru.rknrl"

name := "common-scala"

version := "1.0"

scalaVersion := "2.11.11"

crossScalaVersions := Seq("2.11.11", "2.12.2")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.1"
)