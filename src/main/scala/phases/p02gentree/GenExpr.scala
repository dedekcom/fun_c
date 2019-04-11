package phases.p02gentree

import fun_c.FunCParser
import org.antlr.v4.runtime.ParserRuleContext
import parser.tree._

trait GenExpr {
  this: GenTreeVisitor =>

  def getExpr(ctx: FunCParser.ExpressionContext): FcExpr = ctx.accept(this).asInstanceOf[FcExpr]

  override def visitEx_br(ctx: FunCParser.Ex_brContext): FcNode = ctx.expression().accept(this)

  override def visitEx_val(ctx: FunCParser.Ex_valContext): FcNode = FcValue(ctx.value().accept(this).asInstanceOf[FcLiteral])

  override def visitEx_fun(ctx: FunCParser.Ex_funContext): FcNode = ctx.call_fun().accept(this)

  override def visitCall_fun(ctx: FunCParser.Call_funContext): FcNode = {
    val args = ctx.call_args()
    if (args == null) {
      FcGetVal(namesPath(ctx.namespace_path()))
    } else
      FcCallFun(
        namesPath(ctx.namespace_path()),
        if (args.exp_list() == null)
          List[FcExpr]()
        else
          args.exp_list().accept(this).getAggregate.map(_.asInstanceOf[FcExpr]),
        if (ctx.call_fun() == null)
          Option.empty[FcCallFun]
        else
          Some(ctx.call_fun().accept(this).asInstanceOf[FcCallFun])
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

  def exOpEx(ctx: ParserRuleContext): FcExpr =
    FcExprOpExpr(
      ctx.getChild(0).accept(this).asInstanceOf[FcExpr],
      ctx.getChild(1).getText,
      ctx.getChild(2).accept(this).asInstanceOf[FcExpr]
    )
  override def visitEx_and_ex(ctx: FunCParser.Ex_and_exContext): FcNode = exOpEx(ctx)
  override def visitEx_bitand_ex(ctx: FunCParser.Ex_bitand_exContext): FcNode = exOpEx(ctx)
  override def visitEx_eq_ex(ctx: FunCParser.Ex_eq_exContext): FcNode = exOpEx(ctx)
  override def visitEx_gt_ex(ctx: FunCParser.Ex_gt_exContext): FcNode = exOpEx(ctx)
  override def visitEx_mul_ex(ctx: FunCParser.Ex_mul_exContext): FcNode = exOpEx(ctx)
  override def visitEx_plus_ex(ctx: FunCParser.Ex_plus_exContext): FcNode = exOpEx(ctx)

  override def visitEx_neg_ex(ctx: FunCParser.Ex_neg_exContext): FcNode =
    FcPrefixExpr(
      ctx.prefix.getText,
      getExpr(ctx.expression())
    )
  override def visitEx_not_ex(ctx: FunCParser.Ex_not_exContext): FcNode =
    FcPrefixExpr(
      ctx.prefix.getText,
      getExpr(ctx.expression())
    )

  def getExprBlock(ctx: FunCParser.Expr_blockContext): FcExprBlock = ctx.accept(this).asInstanceOf[FcExprBlock]

  override def visitExpr_block(ctx: FunCParser.Expr_blockContext): FcNode = {
    if (ctx.complex_expr() != null) FcExprBlock(List(ctx.complex_expr().accept(this).asInstanceOf[FcFunStatement]))
    else if (ctx.expression() != null) FcExprBlock(List(ctx.expression().accept(this).asInstanceOf[FcFunStatement]))
    else FcExprBlock(ctx.fun_block().accept(this).getAggregate.map(_.asInstanceOf[FcFunStatement]))
  }

  override def visitLambda_block(ctx: FunCParser.Lambda_blockContext): FcNode =
    if (ctx.expression() != null) FcExprBlock(List(ctx.expression().accept(this).asInstanceOf[FcFunStatement]))
    else FcExprBlock(ctx.fun_block().accept(this).getAggregate.map(_.asInstanceOf[FcFunStatement]))

}
