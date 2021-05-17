val thisProjectVersion = "0.1.0-SNAPSHOT"
val coreScalaVersion = "2.13.5"
val scalaTestVersion = "3.2.7"

ThisBuild / organization := "org.daviscale"
ThisBuild / version      := thisProjectVersion
ThisBuild / scalaVersion := coreScalaVersion

lazy val aggregate = (project in file("."))
  .settings(
    name := "aggregate"
  )
  .aggregate(core)

lazy val core = (project in file("core"))
  .settings(
    name := "core",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
      "org.scalatest" %% "scalatest-flatspec" % scalaTestVersion % "test"
    )
  )

