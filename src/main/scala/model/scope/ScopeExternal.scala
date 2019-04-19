package model.scope

import model.tree.{FcFunc, FcStaticVal, FcStruct}

case class ScopeExternal(
                          extStructs: Map[List[String], FcStruct],
                          extVals:    Map[List[String], FcStaticVal],
                          extFuncs:   Map[List[String], FcFunc]
                        )
