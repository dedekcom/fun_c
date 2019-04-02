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



