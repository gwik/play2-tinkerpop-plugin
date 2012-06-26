import sbt._
import Process._
import Keys._


object PluginBuild extends Build {

  val appName = "play2-tinkerpop"

  val appVersion = "1.0-SNAPSHOT"

  val typesafeReleases = "Typesafe releases" at "http://repo.typesafe.com/typesafe/releases/"

  lazy val root = Project("plugin", base = file("plugin")).settings(
    resolvers += typesafeReleases,
    libraryDependencies ++= Seq (
      "play" %% "play" % "2.0.1",
      "com.tinkerpop.blueprints" % "blueprints-core" % "2.0.0",
      "com.tinkerpop" % "frames" % "2.0.0"
    )
  )

  lazy val orientdbPlugin = Project("orientdb", base = file("impls/orientdb")).
      settings(
    libraryDependencies +=
      "com.tinkerpop.blueprints" % "blueprints-orient-graph" % "2.0.0"
  ).dependsOn(root)

}
