package phases.p02gentree

import fun_c.FunCParser
import model.tree._

trait GenComplexExpr {
  this: GenTreeVisitor =>

  override def visitEx_complex(ctx: FunCParser.Ex_complexContext): FcNode = ctx.complex_expr().accept(this)

  override def visitComplex_expr(ctx: FunCParser.Complex_exprContext): FcNode =
    if(ctx.if_expr() != null) ctx.if_expr().accept(this)
    else if (ctx.lambda_expr() != null) ctx.lambda_expr().accept(this)
    else ctx.match_expr().accept(this)

  override def visitIf_expr(ctx: FunCParser.If_exprContext): FcNode =
    FcIfExpr(
      getExpr(ctx.expression()),
      getExprBlock(ctx.expr_block()),
      getExprBlock(ctx.else_part().expr_block())
    )

  override def visitMatch_expr(ctx: FunCParser.Match_exprContext): FcNode =
    FcMatch(
      getExpr(ctx.expression()),
      ctx.match_cases().accept(this).getAggregate.map(_.asInstanceOf[FcCase])
    )

  override def visitMatch_cases(ctx: FunCParser.Match_casesContext): FcNode = {
    val first = ctx.match_case().accept(this)
    val rest = ctx.match_cases()
    if (rest == null) {
      FcAggregate(List(first))
    } else {
      joinNodes(first, rest.accept(this))
    }
  }

  override def visitMatch_case(ctx: FunCParser.Match_caseContext): FcNode =
    if (ctx.any() != null)
      FcCaseAny(getExprBlock(ctx.expr_block()))
    else
      FcCasePat(
        ctx.exp_list().accept(this).getAggregate.map(_.asInstanceOf[FcExpr]),
        getExprBlock(ctx.expr_block())
      )

  override def visitLambda_expr(ctx: FunCParser.Lambda_exprContext): FcNode =
    FcLambda(
      ctx.lambda_args().accept(this).getAggregate.map(_.asInstanceOf[FcLambdaArg]),
      ctx.lambda_block().accept(this).asInstanceOf[FcExprBlock]
    )

  override def visitLambda_args(ctx: FunCParser.Lambda_argsContext): FcNode =
    if (ctx.id() != null) FcAggregate(List(FcLambdaArgId(getId(ctx.id()))))
    else if (ctx.lambda_args2() == null) FcAggregate(List[FcLambdaArg]())
    else ctx.lambda_args2().accept(this)

  override def visitLambda_args2(ctx: FunCParser.Lambda_args2Context): FcNode = {
    val first = ctx.lambda_arg().accept(this)
    val rest = ctx.lambda_args2()
    if (rest == null) {
      FcAggregate(List(first))
    } else {
      joinNodes(first, rest.accept(this))
    }
  }

  override def visitLambda_arg(ctx: FunCParser.Lambda_argContext): FcNode =
    if(ctx.any() != null) FcLambdaArgAny
    else FcLambdaArgId(getId(ctx.id()))

}
