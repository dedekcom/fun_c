package phases.p01gentree

import fun_c.{FunCBaseVisitor, FunCParser}
import parser.tree._

class GenTreeVisitor extends FunCBaseVisitor[FcNode] with GenBody with GenVals with GenSource with GenExpr {

  def joinNodes(first: FcNode, rest: FcNode): FcNode = FcAggregate(first :: rest.asInstanceOf[FcAggregate].nodes)

  override def visitNamespace_path(ctx: FunCParser.Namespace_pathContext): FcNode = {
    val first = getId(ctx.id())
    val rest = ctx.namespace_path()
    if (rest == null) {
      FcAggregate(List(first))
    } else {
      joinNodes(first, rest.accept(this))
    }
  }

  override def visitFun_single_args(ctx: FunCParser.Fun_single_argsContext): FcNode = {
    val first = ctx.fun_single_arg().accept(this)
    val rest = ctx.fun_single_args()
    if (rest == null) {
      FcAggregate(List(first))
    } else {
      joinNodes(first, rest.accept(this))
    }
  }

  override def visitFun_single_arg(ctx: FunCParser.Fun_single_argContext): FcNode = FcVal(getId(ctx.type_id().id()), getId(ctx.id()))

}
