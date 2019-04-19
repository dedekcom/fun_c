package phases.p03scopes

import model.scope.{ScopeContext, ScopeExternal, ScopeNamespace}
import model.tree._
import phases.p01preproc.CompilerRunner

class GenScopes(runner: CompilerRunner) {

  val scopes: Map[String, ScopeContext] = runner.documents.toList.foldLeft(Map[String, ScopeContext]()) {
      case (curScopes, (key, parser)) => curScopes +
        (key -> genScope(List(parser.source.name), parser.source, ScopeExternal(Map(), Map(), Map())))
    }

  def genScope(path: List[String], source: FcSource, extern: ScopeExternal): ScopeContext = {
    val (namespace, updExtern) = source.body.foldLeft(
      ScopeNamespace(source.namespace, path, source.includes, List(), List(), List(), List(), List(), 0),
      extern
      ) {
      case ((nms, ext), next) => next match {

        case ln: FcLocalNamespace =>
          val locSrc = FcSource(FcNamespace(List(ln.name), None), List(), ln.body)
          val sc = genScope(locSrc.name :: path, locSrc, ext)
          (
            nms.copy(scopes = sc.scope :: nms.scopes),
            sc.external
          )

        case sv: FcStaticVal =>
          (
            nms.copy(values = sv :: nms.values),
            if (sv.isExtern)
              ext.copy(extVals = ext.extVals + ((sv.name :: nms.path) -> sv))
            else
              ext
          )

        case st: FcStruct =>
          (
            nms.copy(structs = st :: nms.structs),
            if (st.isExtern)
              ext.copy(extStructs = ext.extStructs + ((st.name :: nms.path) -> st))
            else
              ext
          )

        case fn: FcFunc =>
          (
            nms.copy(funcs = fn :: nms.funcs),
            if (fn.isExtern)
              ext.copy(extFuncs = ext.extFuncs + ((fn.name :: nms.path) -> fn))
            else
              ext
          )

        case _ => (nms, ext)
      }
    }
    ScopeContext(
      updExtern,
      namespace.copy(body = source.body)
    )
  }

}
