package model.tree

case class FcSource(namespace: FcNamespace, includes: List[FcInclude], body: List[FcBodyStatement]) extends FcNode {
  val name: String = namespace.path.head.id
}

case class FcNamespace(path: List[FcId], alias: Option[FcId]) extends FcNode

case class FcInclude(path: List[FcId]) extends FcNode {
  val fullNamespace: String = path.map(_.id).mkString(".")

  val namespace: FcId = path.last

  def getFile: String = path.dropRight(1).map(_.id).mkString("/") + ".fc"
}


sealed abstract class FcBodyStatement extends FcNode { this: Product => }

case class FcLocalNamespace(name: FcId, body: List[FcBodyStatement]) extends FcBodyStatement

case class FcCBlock(block: String)                          extends FcBodyStatement

case class FcStaticVal(isExtern: Boolean, declaredVal: FcLocalVal) extends FcBodyStatement {
  val name: String = declaredVal.declare.name.id
}

case class FcStruct(isExtern: Boolean, declaredStruct: FcLocalStruct) extends FcBodyStatement {
  val name: String = declaredStruct.id.id
}

case class FcFunc(isExtern: Boolean, declaredFunc: FcLocalFunc) extends FcBodyStatement {
  val name: String = declaredFunc.name.id
}
