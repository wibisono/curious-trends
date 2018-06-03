package org.wongiseng.shared.rest

import com.avsystem.commons.serialization.GenCodec

case class Node(id: String, name:String, group: Int)
object Node {
  implicit val nodeCodec : GenCodec[Node] = GenCodec.materialize
}
case class Link(source: String, target: String, value:Int)
object Link {
  implicit val nodeCodec : GenCodec[Link] = GenCodec.materialize
}

case class Graph(nodes: Seq[Node], links: Seq[Link])
object Graph {
  implicit val nodeCodec : GenCodec[Graph] = GenCodec.materialize
}
