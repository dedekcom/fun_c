package phases.p01gentree

import fun_c.FunCParser
import parser.tree._

import scala.collection.JavaConverters._

trait GenSource {
  this: GenTreeVisitor =>

  override def visitSource(ctx: FunCParser.SourceContext): FcNode =
    FcSource(
      ctx.h_namespace().accept(this).asInstanceOf[FcNamespace],
      ctx.include().asScala.toList.map(_.accept(this).asInstanceOf[FcInclude]),
      gotoBody(ctx.body())
    )

  override def visitInclude(ctx: FunCParser.IncludeContext): FcNode =
    FcInclude(ctx.namespace_path().accept(this).getAggregate.map(_.asInstanceOf[FcId]))

  override def visitH_namespace(ctx: FunCParser.H_namespaceContext): FcNode =
    FcNamespace(ctx.namespace_path().accept(this).getAggregate.map(_.asInstanceOf[FcId]))

}
