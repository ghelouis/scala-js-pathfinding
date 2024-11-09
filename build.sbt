ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

enablePlugins(ScalaJSPlugin)

lazy val root = (project in file("."))
  .settings(
    name := "scala-js-pathfinding",
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.8.0"
  )
