package parser.tree

abstract class FcNode {
  this: Product =>
  def getAggregate: List[FcNode] = Nil

  def caseClassName: String = productPrefix
}

case class FcAggregate(nodes: List[FcNode]) extends FcNode {
  override def getAggregate: List[FcNode] = nodes
}

case class FcId(id: String, meta: TokenMeta)  extends FcNode

case class FcType(id: FcId, param: Option[FcType]) extends FcNode

case class FcVal(vtype: FcType, name: FcId)     extends FcNode

abstract class FcLiteral extends FcNode { this: Product => }
case class FcNum(num: String)                 extends FcLiteral
case class FcFloat(num: String)               extends FcLiteral
case class FcStr(str: String)                 extends FcLiteral
case object FcNull                            extends FcLiteral
case class FcChar(char: Char)                 extends FcLiteral
