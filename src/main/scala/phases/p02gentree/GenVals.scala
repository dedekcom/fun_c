package phases.p02gentree

import fun_c.FunCParser
import model.tree._

trait GenVals {
  this: GenTreeVisitor =>
  val codePathFile: String

  def getId(ctx: FunCParser.IdContext): FcId = ctx.accept(this).asInstanceOf[FcId]

  override def visitId(ctx: FunCParser.IdContext): FcNode = FcId(ctx.getText, TokenMeta(codePathFile, ctx))

  override def visitV_char(ctx: FunCParser.V_charContext): FcNode = FcChar(ctx.getText.charAt(1))

  override def visitV_float(ctx: FunCParser.V_floatContext): FcNode = FcFloat(ctx.getText)

  override def visitV_null(ctx: FunCParser.V_nullContext): FcNode = FcNull

  override def visitV_num(ctx: FunCParser.V_numContext): FcNode = FcNum(ctx.getText)

  override def visitV_str(ctx: FunCParser.V_strContext): FcNode = FcStr(ctx.getText.substring(1, ctx.getText.length - 1))

}
