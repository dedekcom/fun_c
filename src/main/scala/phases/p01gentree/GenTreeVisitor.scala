package phases.p01gentree

import fun_c.FunCBaseVisitor
import parser.tree._

class GenTreeVisitor extends FunCBaseVisitor[FcNode] with GenBody with GenVals with GenSource with GenExpr {

  def joinNodes(first: FcNode, rest: FcNode): FcNode = FcAggregate(first :: rest.asInstanceOf[FcAggregate].nodes)

}
