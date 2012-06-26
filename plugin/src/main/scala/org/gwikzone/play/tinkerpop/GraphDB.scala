package org.gwikzone.play.tinkerpop

import com.tinkerpop.blueprints.{Graph,TransactionalGraph}
import play.api.Application
import play.api.Play.current
import com.tinkerpop.blueprints.IndexableGraph
import com.tinkerpop.frames.FramedGraph
import scala.reflect.Manifest
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.Edge
import com.tinkerpop.blueprints.Index
import com.tinkerpop.blueprints.Element
import com.tinkerpop.blueprints.util.IndexableGraphHelper
import com.tinkerpop.blueprints.Parameter


trait TransactionalGraphDBAPI[G <: TransactionalGraph] extends GraphDBAPI[G] {

  def withTransaction[T](block: G => T)(implicit app: Application, m: Manifest[G]): T = {
	graph.startTransaction
    try {
      val result = block(graph)
      graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS)
      result
    } catch {
      case e: Exception => {
        graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE)
        throw e
      }
    }
  }

}

trait IndexableGraphDBAPI[G <: IndexableGraph] extends GraphDBAPI[G] {

  // XXX: add index configuration parameter
  def getOrCreateIndex[T <: Element](indexName: String, cls: Class[T], params:Parameter[_,_]*): Index[T] = {
    val index = graph.getIndex(indexName, cls)
    index match {
      case null => graph.createIndex(indexName, cls, params:_*)
      case _ => index
    }
  }
  
  def addUniqueVertex(id: Any, index: Index[Vertex], uniqueKey: String, uniqueValue: Any): Vertex = {
    val iterator = index.get(uniqueKey, uniqueValue).iterator
    if (iterator.hasNext) {
      iterator.next
    } else {
      val vertex = graph.addVertex(id)
      vertex.setProperty(uniqueKey, uniqueValue)
      vertex
    }
  }

  def addUniqueEdge(id: Any, index: Index[Edge], uniqueKey: String, uniqueValue: Any,
      outVertex: Vertex, inVertex: Vertex, label: String): Edge = {
    val iterator = index.get(uniqueKey, uniqueValue).iterator
    if (iterator.hasNext) {
      iterator.next
    } else {
      val vertex = graph.addEdge(id, outVertex, inVertex, label)
      vertex.setProperty(uniqueKey, uniqueValue)
      vertex
    }
  }

}
