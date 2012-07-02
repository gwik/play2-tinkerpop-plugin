package org.gwikzone.play.tinkerpop

import com.tinkerpop.blueprints.Graph
import play.api._
import play.api.Play.current
import com.tinkerpop.frames.FramedGraph


trait GraphFactory[G <: Graph] {
	// def apply: G
}

trait GraphDBAPI[G <: Graph] {

  implicit val man: Manifest[G]

  def app(implicit application: Application) = {
    application
  }

  lazy val plugin = {
    app.plugin[BlueprintsPlugin[G]].getOrElse(
        throw new RuntimeException("Blueprint plugin not present at runtime.")
    )
  }

  lazy val graph: G = plugin.graph
  lazy val framedGraph: FramedGraph[G] = new FramedGraph(graph)

}

trait BlueprintsPlugin[G <: Graph] extends Plugin {

  val graph: G

  override
  def onStart = {
    // load the graph if of lazy
    graph
  }

  override
  def onStop = {
    Logger("blueprint graph plugin").info("shutdown grapdb.")
    graph.shutdown()
  }

  override
  def enabled = true

}

class BlueprintsConfig(app: Application) {
    val config = app.configuration.getConfig("tinkerpop.blueprints").
  	getOrElse(throw app.configuration.reportError("tinkerpop",
  				"no tinkerpop configuration found"))
}

