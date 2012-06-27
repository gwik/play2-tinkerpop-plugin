# Play 2 Tinkerpop Blueprints graph plugin

A GraphDB plugin for [Play 2 framework](http://playframework.org/) using the
[Tinkerpop blueprints stack](https://github.com/tinkerpop/blueprints/wiki/).

## Installation

### Plugins declaration

Add this to your _conf/play.plugins_ file, create it if it's not there already.

  1000:org.gwikzone.play.tinkerpop.blueprints.orientdb.OrientBlueprintPlugin

### Instanciate your graph db

  package utils

  import org.gwikzone.play.tinkerpop.blueprints.orientdb.OrientDB
  object GraphDB extends OrientDB

Import GraphDB from this package in your application code. Then if you want to
use an other graphDB say neo4j you just need to change the import in this
and you're done.
