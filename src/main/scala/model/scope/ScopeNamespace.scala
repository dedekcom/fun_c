package model.scope

import model.tree._

case class ScopeNamespace(
                           namespace: FcNamespace,
                           path: List[FcId],
                           includes: List[FcInclude],
                           scopes: List[ScopeNamespace],
                           values: Map[List[String], FcStaticVal],
                           structs: List[FcStruct],
                           funcs: List[FcFunc],
                           body: List[FcBodyStatement]
                         ) {
  lazy val name: String = namespace.alias match {
    case Some(FcId(n, _)) => n
    case None => namespace.path.map(_.id).mkString(".")
  }

  lazy val isLocal: Boolean = namespace.path.length == 1

}


/*
case class FcLocalNamespace(name: FcId, body: List[FcBodyStatement]) extends FcBodyStatement

case class FcCBlock(block: String)                          extends FcBodyStatement

case class FcStaticVal(isExtern: Boolean, declaredVal: FcLocalVal) extends FcBodyStatement

case class FcStruct(isExtern: Boolean, declaredStruct: FcLocalStruct) extends FcBodyStatement

case class FcFunc(isExtern: Boolean, declaredFunc: FcLocalFunc) extends FcBodyStatement
 */