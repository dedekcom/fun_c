package model.tree

import org.antlr.v4.runtime.ParserRuleContext

case class TokenMeta(file: String, line: Int, range: Range)

object TokenMeta {

  val EmptyToken = TokenMeta("", 0, Range(0, 0))

  def apply(file: String, ctx: ParserRuleContext): TokenMeta = {
    val start = ctx.start.getCharPositionInLine
    TokenMeta(
      file,
      ctx.start.getLine,
      start until (start + ctx.start.getText.length))
  }

}
