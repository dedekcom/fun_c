package parser

import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import fun_c._
import parser.tree.FcSource
import phases.p01gentree.GenTreeVisitor
import utils.io.FileUtil

object Parser {

  def fromFile(inputRdl: String): Parser = new Parser(FileUtil.load(inputRdl))

  def apply(code: String): Parser = new Parser(code)

}

class Parser(val code: String) {

  val lexer             = new FunCLexer(CharStreams.fromString(code))
  val commonTokenStream = new CommonTokenStream(lexer)
  val parser            = new FunCParser(commonTokenStream)
  val visitor           = new GenTreeVisitor()
  val tree: FcSource    = visitor.visit(parser.source()).asInstanceOf[FcSource]

}
