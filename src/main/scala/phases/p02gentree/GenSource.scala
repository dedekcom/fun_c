package phases.p02gentree

import fun_c.FunCParser
import parser.tree._

import scala.collection.JavaConverters._

trait GenSource {
  this: GenTreeVisitor =>

  override def visitSource(ctx: FunCParser.SourceContext): FcNode =
    FcSource(
      ctx.h_namespace().accept(this).asInstanceOf[FcNamespace],
      ctx.include().asScala.toList.map(_.accept(this).asInstanceOf[FcInclude]),
      getBodyStatements(ctx.body_statement().asScala.toList)
    )

  override def visitInclude(ctx: FunCParser.IncludeContext): FcNode =
    FcInclude(namesPath(ctx.namespace_path()))

  override def visitH_namespace(ctx: FunCParser.H_namespaceContext): FcNode =
    FcNamespace(namesPath(ctx.namespace_path()))

}
