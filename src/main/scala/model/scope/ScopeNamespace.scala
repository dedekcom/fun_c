package model.scope

import model.tree._

case class ScopeNamespace(
                           namespace: FcNamespace,
                           path: List[String],
                           includes: List[FcInclude],
                           scopes: List[ScopeNamespace],
                           values: Map[List[String], FcStaticVal],
                           structs: Map[List[String], FcStruct],
                           funcs: Map[List[String], FcFunc],
                           body: List[FcBodyStatement],
                           cntLocalFuncs: Int
                         ) {
  val name: String = namespace.alias match {
    case Some(FcId(n, _)) => n
    case None => namespace.path.map(_.id).mkString(".")
  }

  val isLocal: Boolean = namespace.path.length == 1

}
