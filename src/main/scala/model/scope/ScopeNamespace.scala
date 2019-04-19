package model.scope

import model.tree._

case class ScopeNamespace(
                           namespace: FcNamespace,
                           path: List[String],
                           includes: List[FcInclude],
                           scopes: List[ScopeNamespace],
                           values: List[FcStaticVal],
                           structs: List[FcStruct],
                           funcs: List[FcFunc],
                           body: List[FcBodyStatement],
                           cntLocalFuncs: Int
                         ) {
  lazy val name: String = namespace.alias match {
    case Some(FcId(n, _)) => n
    case None => namespace.path.map(_.id).mkString(".")
  }

  lazy val isLocal: Boolean = namespace.path.length == 1

}
