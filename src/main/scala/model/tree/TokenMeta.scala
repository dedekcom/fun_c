package model.tree

import org.antlr.v4.runtime.ParserRuleContext

case class TokenMeta(line: Int, range: Range)

object TokenMeta {

  val EmptyToken = TokenMeta(0, Range(0, 0))

  def apply(ctx: ParserRuleContext): TokenMeta = {
    val start = ctx.start.getCharPositionInLine
    TokenMeta(
      ctx.start.getLine,
      start until (start + ctx.start.getText.length))
  }

}
