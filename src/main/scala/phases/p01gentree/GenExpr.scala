package phases.p01gentree

import fun_c.FunCParser
import parser.tree.FcExpr

trait GenExpr {
  this: GenTreeVisitor =>

  def getExpr(ctx: FunCParser.ExpressionContext): FcExpr = ctx.accept(this).asInstanceOf[FcExpr]

}
