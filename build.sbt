import sbt.Resolver

name := "search-front"

version := "0.1"

scalaVersion := "2.13.7"
scalacOptions ++= Seq("-deprecation")

resolvers += Resolver.sonatypeRepo("releases")
resolvers += "Local Maven Repository" at "file:///" + Path.userHome + "/.m2/repository"

// you need to "install" (mvn install) search-back before it will be available here
lazy val searchBack = Seq(
  "org.ionkin" % "core" % "1.3-SNAPSHOT",
  "org.ionkin" % "bool-parser" % "1.4-SNAPSHOT",
  "org.ionkin" % "index" % "1.3-SNAPSHOT"
)

libraryDependencies ++= searchBack
libraryDependencies += guice

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, UniversalPlugin)

javaOptions in Universal ++= Seq(
  "-J-Xmx5024m",
  "-J-Xms1024m"
)

