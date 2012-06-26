package org.gwikzone.play.tinkerpop.blueprints.orientdb

import org.gwikzone.play.tinkerpop.{BlueprintsConfig,GraphFactory,BlueprintsPlugin}
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import play.api.{Configuration,Logger,Application}
import org.gwikzone.play.tinkerpop.GraphDBAPI
import org.gwikzone.play.tinkerpop.TransactionalGraphDBAPI
import org.gwikzone.play.tinkerpop.IndexableGraphDBAPI
import scala.reflect.Manifest


object OrientGraphFactory extends GraphFactory[OrientGraph] {
  def apply(config: Configuration): OrientGraph = {
    val dbUrl = config.getString("orientdb.url").getOrElse(
        throw config.reportError("orientdb.url",
            "missing blueprints.orientdb.url"))
    Logger("orientdb blueprints plugin").info(
        "opening orientdb database at " + dbUrl)
    new OrientGraph(dbUrl)
  }
}

abstract class OrientDB extends GraphDBAPI[OrientGraph]
		with TransactionalGraphDBAPI[OrientGraph]
		with IndexableGraphDBAPI[OrientGraph] {

  val man: Manifest[OrientGraph] = implicitly
}

class OrientBlueprintPlugin(app: Application)
	extends BlueprintsConfig(app)
	with BlueprintsPlugin[OrientGraph] {

  type G = OrientGraph
  lazy val graph = OrientGraphFactory(config)

}
