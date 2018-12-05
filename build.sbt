import sbt.Resolver

name := "learn-play"

version := "0.1"

scalaVersion := "2.12.6"
scalacOptions ++= Seq("-deprecation")

resolvers += Resolver.sonatypeRepo("releases")
resolvers += "Local Maven Repository" at "file:///"+Path.userHome+"/.m2/repository"

libraryDependencies ++= Seq(
  "org.ionkin" % "core" % "1.3-SNAPSHOT",
  "org.ionkin" % "bool-parser" % "1.4-SNAPSHOT",
  "org.ionkin" % "index" % "1.3-SNAPSHOT"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies += guice
