name := "hmrctest"

version := "0.1"

scalaVersion := "2.12.8"

lazy val allDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.8" % "test")

lazy val hmrctest = (project in file("."))
  .settings(libraryDependencies ++= allDependencies)