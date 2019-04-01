package parser.tree

abstract class FcNode {
  this: Product =>
  def getAggregate: List[FcNode] = Nil

  def caseClassName: String = productPrefix
}

case class FcAggregate(nodes: List[FcNode]) extends FcNode {
  override def getAggregate: List[FcNode] = nodes
}

case class FcId(id: String, meta: TokenMeta) extends FcNode

case class FcNamespace(path: List[FcId]) extends FcNode

case class FcInclude(path: List[FcId]) extends FcNode

case class FcSource(namespace: FcNamespace, includes: List[FcInclude], body: List[FcNode]) extends FcNode

case class FcCBlock(block: String) extends FcNode
