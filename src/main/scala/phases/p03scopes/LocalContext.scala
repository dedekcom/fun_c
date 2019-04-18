package phases.p03scopes

import model.scope.ScopeNamespace
import model.tree._

case class LocalContext(
                        extStructs: Map[List[String], FcStruct],
                        extVals: Map[List[String], FcStaticVal],
                        extFuncs: Map[List[String], FcFunc],
                        scopes: Map[List[String], ScopeNamespace]
                       )
