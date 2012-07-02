package org.gwikzone.play.tinkerpop

import com.tinkerpop.blueprints.TransactionalGraph
import play.api.Application
import com.tinkerpop.blueprints.IndexableGraph
import scala.reflect.Manifest
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.Edge
import com.tinkerpop.blueprints.Index
import com.tinkerpop.blueprints.Element
import com.tinkerpop.blueprints.Parameter


trait TransactionalGraphDBAPI[G <: TransactionalGraph] extends GraphDBAPI[G] {

  def withTransaction[T](block: => T): T = {
	graph.startTransaction
    try {
      val result = block
      graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS)
      result
    } catch {
      case e: Exception => {
        graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE)
        throw e
      }
    }
  }

  def withRollback[T](block: => T): T = {
    graph.startTransaction
    try {
      val result = block
      graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE)
      result
    } catch {
      case e: Exception =>
        graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE)
        throw e
    }
  }

}

trait IndexableGraphDBAPI[G <: IndexableGraph] extends GraphDBAPI[G] {

  def getOrCreateIndex[T <: Element](indexName: String, cls: Class[T], params:Parameter[_,_]*): Index[T] = {
    val index = Option(graph.getIndex(indexName, cls))
    index.getOrElse { graph.createIndex(indexName, cls, params:_*) }
  }

  def addUniqueVertex(id: Any, index: Index[Vertex], uniqueKey: String, uniqueValue: Any): Vertex = {
    val iterator = index.get(uniqueKey, uniqueValue).iterator
    if (iterator.hasNext) {
      iterator.next
    } else {
      val vertex = graph.addVertex(id)
      index.put(uniqueKey, uniqueValue, vertex)
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
      val edge = graph.addEdge(id, outVertex, inVertex, label)
      index.put(uniqueKey, uniqueValue, edge)
      edge.setProperty(uniqueKey, uniqueValue)
      edge
    }
  }

}
