package model.tree

case class FcSource(namespace: FcNamespace, includes: List[FcInclude], body: List[FcBodyStatement]) extends FcNode

case class FcNamespace(path: List[FcId], alias: Option[FcId]) extends FcNode

case class FcInclude(path: List[FcId]) extends FcNode {
  lazy val fullNamespace: String = path.map(_.id).mkString(".")

  lazy val namespace: FcId = path.last

  def getFile: String = path.dropRight(1).map(_.id).mkString("/") + ".fc"
}


abstract class FcBodyStatement extends FcNode { this: Product => }

case class FcLocalNamespace(name: FcId, body: List[FcBodyStatement]) extends FcBodyStatement

case class FcCBlock(block: String)                          extends FcBodyStatement

case class FcStaticVal(isExtern: Boolean, declaredVal: FcLocalVal) extends FcBodyStatement

case class FcStruct(isExtern: Boolean, declaredStruct: FcLocalStruct) extends FcBodyStatement

case class FcFunc(isExtern: Boolean, declaredFunc: FcLocalFunc) extends FcBodyStatement
