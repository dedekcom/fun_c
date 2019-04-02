package phases.p01gentree

import fun_c.FunCParser
import org.antlr.v4.runtime.ParserRuleContext
import parser.tree.{FcId, FcNode, TokenMeta}

trait GenVals {
  this: GenTreeVisitor =>

  def getId(ctx: ParserRuleContext): FcId = ctx.accept(this).asInstanceOf[FcId]

  override def visitId(ctx: FunCParser.IdContext): FcNode = FcId(ctx.getText, TokenMeta(ctx))

}
