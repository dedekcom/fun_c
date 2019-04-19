package utils

import fun_c._
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import model.tree.FcSource
import phases.p02gentree.GenTreeVisitor
import utils.io.FileUtil

object Parser {

  def fromFile(filename: String): Parser = new Parser(filename, FileUtil.load(filename))

}

class Parser(val filename: String, val code: String) {

  val lexer             = new FunCLexer(CharStreams.fromString(code))
  val commonTokenStream = new CommonTokenStream(lexer)
  val parser            = new FunCParser(commonTokenStream)
  val visitor           = new GenTreeVisitor(filename)
  val source: FcSource  = visitor.visit(parser.source()).asInstanceOf[FcSource]

}
