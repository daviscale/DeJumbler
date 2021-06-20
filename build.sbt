val versions = new {
  val thisProjectVersion = "0.1.0-SNAPSHOT"
  val coreScalaVersion = "2.13.5"
  val scalaTestVersion = "3.2.7"
  val akkaVersion = "2.6.14"
  val akkaHttpVersion = "10.2.4"
}

ThisBuild / organization := "org.daviscale"
ThisBuild / version      := versions.thisProjectVersion
ThisBuild / scalaVersion := versions.coreScalaVersion

lazy val aggregate = (project in file("."))
  .settings(
    name := "aggregate"
  )
  .aggregate(core, api)

lazy val core = (project in file("core"))
  .settings(
    name := "core"
  )

lazy val api = (project in file("api"))
  .settings(
    name := "api",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % versions.scalaTestVersion % "test",
      "org.scalatest" %% "scalatest-flatspec" % versions.scalaTestVersion % "test",
      "com.typesafe.akka" %% "akka-testkit" % versions.akkaVersion % "test",
      "com.typesafe.akka" %% "akka-http" % versions.akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % versions.akkaVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % versions.akkaHttpVersion
    )
  )
