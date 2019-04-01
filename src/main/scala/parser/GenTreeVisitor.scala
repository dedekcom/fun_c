package parser

import fun_c.{FunCBaseVisitor, FunCParser}
import parser.tree._

import scala.collection.JavaConverters._

class GenTreeVisitor extends FunCBaseVisitor[FcNode] {

  def joinNodes(first: FcNode, rest: FcNode): FcNode = FcAggregate(first :: rest.asInstanceOf[FcAggregate].nodes)

  override def visitC_block(ctx: FunCParser.C_blockContext): FcNode =
    FcCBlock(ctx.C_BODY().getText)

  override def visitInclude(ctx: FunCParser.IncludeContext): FcNode =
    FcInclude(ctx.namespace_path().accept(this).getAggregate.map(_.asInstanceOf[FcId]))

  override def visitNamespace(ctx: FunCParser.NamespaceContext): FcNode =
    FcNamespace(ctx.namespace_path().accept(this).getAggregate.map(_.asInstanceOf[FcId]))

  override def visitNamespace_path(ctx: FunCParser.Namespace_pathContext): FcNode = {
    val first = FcId(ctx.ID().getText)
    val rest = ctx.namespace_path()
    if (rest == null) {
      FcAggregate(List(first))
    } else {
      joinNodes(first, rest.accept(this))
    }
  }

  override def visitSource(ctx: FunCParser.SourceContext): FcNode =
    FcSource(
      ctx.namespace().accept(this).asInstanceOf[FcNamespace],
      ctx.include().asScala.toList.map(_.accept(this).asInstanceOf[FcInclude]),
      ctx.body().asScala.toList.map(_.accept(this))
    )

}
