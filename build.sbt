name := "learn-play"

version := "0.1"

scalaVersion := "2.12.5"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies += guice
//libraryDependencies += "com.typesafe.play" %% "play" % "2.6.12"