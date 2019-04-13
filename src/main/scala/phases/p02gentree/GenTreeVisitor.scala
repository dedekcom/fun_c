package phases.p02gentree

import fun_c.{FunCBaseVisitor, FunCParser}
import model.tree._

class GenTreeVisitor(val codePathFile: String) extends FunCBaseVisitor[FcNode] with GenBody with GenVals with GenSource with GenExpr
  with GenFun with GenComplexExpr {

  def joinNodes(first: FcNode, rest: FcNode): FcNode = FcAggregate(first :: rest.asInstanceOf[FcAggregate].nodes)

  def namesPath(ctx: FunCParser.Namespace_pathContext): List[FcId] = ctx.accept(this).getAggregate.map(_.asInstanceOf[FcId])

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

  override def visitFun_single_arg(ctx: FunCParser.Fun_single_argContext): FcNode =
    FcVal(getType(ctx.type_id()), getId(ctx.id()))

}
