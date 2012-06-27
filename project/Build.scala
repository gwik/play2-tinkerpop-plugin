import sbt._
import Keys._
import java.net.URL

object PluginBuild extends Build {

  val typesafeReleases = "Typesafe releases" at "http://repo.typesafe.com/typesafe/releases/"

  val commonSettings = Seq(
    organization := "org.gwikzone",
    version := "1.0-SNAPSHOT",
    licenses := Seq(("Apache License, Version 2.0",
        new URL("http://www.apache.org/licenses/LICENSE-2.0.html")))
  )

  lazy val root = Project("play2-tinkerpop-plugin",
      base = file("plugin"),
      settings = Project.defaultSettings ++ commonSettings).settings(
    resolvers += typesafeReleases,
    libraryDependencies ++= Seq (
      "play" %% "play" % "2.0.1",
      "com.tinkerpop.blueprints" % "blueprints-core" % "2.0.0",
      "com.tinkerpop" % "frames" % "2.0.0"
    )
  )

  lazy val orientdbPlugin = Project("play2-tinkerpop-plugin-orientdb",
      base = file("impls/orientdb"),
      settings = Project.defaultSettings ++ commonSettings).settings(
    libraryDependencies +=
      "com.tinkerpop.blueprints" % "blueprints-orient-graph" % "2.0.0"
  ).dependsOn(root)

}
