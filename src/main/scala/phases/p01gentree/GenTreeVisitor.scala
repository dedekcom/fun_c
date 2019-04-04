package phases.p01gentree

import fun_c.{FunCBaseVisitor, FunCParser}
import parser.tree._

class GenTreeVisitor extends FunCBaseVisitor[FcNode] with GenBody with GenVals with GenSource with GenExpr {

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

  override def visitFun_single_arg(ctx: FunCParser.Fun_single_argContext): FcNode = FcVal(getId(ctx.type_id().id()), getId(ctx.id()))

  override def visitEx_br(ctx: FunCParser.Ex_brContext): FcNode = ctx.expression().accept(this)
  override def visitEx_val(ctx: FunCParser.Ex_valContext): FcNode = FcValue(ctx.value().accept(this).asInstanceOf[FcLiteral])

  override def visitEx_fun(ctx: FunCParser.Ex_funContext): FcNode = {
    val args = ctx.call_fun().call_args()
    if (args == null) {
      FcGetVal(namesPath(ctx.call_fun().namespace_path()))
    } else
    FcCallFun(
      namesPath(ctx.call_fun().namespace_path()),
      args.exp_list().accept(this).getAggregate.map(_.asInstanceOf[FcExpr])
    )
  }

  override def visitCall_args(ctx: FunCParser.Call_argsContext): FcNode =
    if(ctx.exp_list() == null) FcAggregate(List[FcExpr]())
    else ctx.exp_list().accept(this)

  override def visitExp_list(ctx: FunCParser.Exp_listContext): FcNode = {
    val first = ctx.expression().accept(this)
    val rest = ctx.exp_list()
    if (rest == null) {
      FcAggregate(List(first))
    } else {
      joinNodes(first, rest.accept(this))
    }
  }

  override def visitLocal_struct(ctx: FunCParser.Local_structContext): FcNode =
    FcStruct(
      isExtern = false,
      getId(ctx.id()),
      ctx.fun_single_args().accept(this).getAggregate.map(_.asInstanceOf[FcVal])
    )

}
