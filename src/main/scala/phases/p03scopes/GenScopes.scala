package phases.p03scopes

import model.scope.{ScopeContext, ScopeExternal, ScopeNamespace}
import model.tree._
import phases.p01preproc.CompilerRunner

class GenScopes(runner: CompilerRunner) {

  def addPair[K, V](acc: Map[K, V], key: K, value: V): Map[K, V] = acc.get(key) match {
    case Some(v) => throw new RuntimeException(s"cannot add $key because value already exists")
    case None => acc.updated(key, value)
  }

  val scopes: Map[String, ScopeContext] = runner.documents.toList.foldLeft(Map[String, ScopeContext]()) {
      case (curScopes, (key, parser)) =>
        val sc = genScope(List(parser.source.name), parser.source, ScopeExternal(Map(), Map(), Map()))
        parser.source.namespace.alias match {

          case Some(FcId(id, _)) =>
            val sc1 = addPair(curScopes, key, sc)
            addPair(sc1, id, sc)

          case None => addPair(curScopes, key, sc)
        }

    }

  def emptyConstructor(name: FcId): FcLocalFunc = FcLocalFunc(FcType(FcId("Void", TokenMeta.EmptyToken), None),
    FcId(name.id + "_constructor", name.meta), List(), None, List())

  def genScope(path: List[String], source: FcSource, extern: ScopeExternal): ScopeContext = {
    val (namespace, updExtern, constructorFun) = source.body.foldLeft(
      ScopeNamespace(source.namespace, path, source.includes, List(), Map(), Map(), Map(), List(), None, 0),
      extern,
      List[FcLocalVal]()
      ) {
      case ((nms, ext, constr), next) => next match {

        case ln: FcLocalNamespace =>
          val locSrc = FcSource(FcNamespace(List(ln.name), None), List(), ln.body)
          val sc = genScope(locSrc.name :: path, locSrc, ext)
          (
            nms.copy(scopes = sc.scope :: nms.scopes),
            sc.external,
            constr
          )

        case sv: FcStaticVal =>
          (
            nms.copy(values = addPair(nms.values, sv.name :: path, sv)),
            if (sv.isExtern)
              ext.copy(extVals = addPair(ext.extVals, sv.name :: nms.path, sv))
            else
              ext,
            sv.declaredVal :: constr
          )

        case st: FcStruct =>
          (
            nms.copy(structs = addPair(nms.structs, st.name :: nms.path, st)),
            if (st.isExtern)
              ext.copy(extStructs = addPair(ext.extStructs, st.name :: nms.path, st))
            else
              ext,
            constr
          )

        case fn: FcFunc =>
          (
            nms.copy(funcs = addPair(nms.funcs, fn.name :: nms.path, fn)),
            if (fn.isExtern)
              ext.copy(extFuncs = addPair(ext.extFuncs, fn.name :: nms.path, fn))
            else
              ext,
            constr
          )

        case _ => (nms, ext, constr)
      }
    }
    val constructor = FcFunc(isExtern = false, emptyConstructor(source.namespace.path.last).copy(body = constructorFun))
    ScopeContext(
      updExtern,
      namespace.copy(
        body = source.body,
        constructor = Some(constructor.declaredFunc)
      )
    )
  }

}
