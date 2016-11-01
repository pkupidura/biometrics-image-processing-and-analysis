name := """hello-scala"""

version := "1.0"

scalaVersion in ThisBuild := "2.11.7"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
libraryDependencies += "org.scala-lang" % "scala-swing" % "2.11.0-M7"

fork in run := true
