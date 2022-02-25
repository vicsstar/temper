import Dependencies._

name := """temper"""
organization := "com.vicigbokwe.temper"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.8"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
libraryDependencies ++= Seq(
  akka,
  slick,
  ws,
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.vicigbokwe.temper.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.vicigbokwe.temper.binders._"
