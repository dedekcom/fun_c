package parser.tree

case class FcSource(namespace: FcNamespace, includes: List[FcInclude], body: List[FcBodyStatement]) extends FcNode

case class FcNamespace(path: List[FcId]) extends FcNode

case class FcInclude(path: List[FcId]) extends FcNode


abstract class FcBodyStatement extends FcNode { this: Product => }

case class FcLocalNamespace(name: FcId, body: List[FcNode]) extends FcBodyStatement

case class FcCBlock(block: String)                          extends FcBodyStatement

case class FcStaticVal(isExtern: Boolean, isLazy: Boolean, value: FcVal, assign: FcExpr) extends FcBodyStatement

case class FcStruct(isExtern: Boolean, id: FcId, vals: List[FcVal]) extends FcBodyStatement