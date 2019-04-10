package phases.p02gentree

import fun_c.FunCParser
import parser.tree._
import scala.collection.JavaConverters._

trait GenBody {
  this: GenTreeVisitor =>

  def getBodyStatements(ctx: List[FunCParser.Body_statementContext]): List[FcBodyStatement] =
    ctx.map(_.accept(this).asInstanceOf[FcBodyStatement])

  override def visitC_block(ctx: FunCParser.C_blockContext): FcNode =
    FcCBlock(ctx.C_BODY().getText)

  override def visitLocal_namespace(ctx: FunCParser.Local_namespaceContext): FcNode =
    FcLocalNamespace(
      getId(ctx.id()),
      getBodyStatements(ctx.body_statement().asScala.toList)
    )

  override def visitStatic_val(ctx: FunCParser.Static_valContext): FcNode =
    FcStaticVal(
      ctx.extern() != null,
      ctx.declare_val().accept(this).asInstanceOf[FcLocalVal]
    )

  override def visitDeclare_struct(ctx: FunCParser.Declare_structContext): FcNode =
    FcStruct(
      ctx.extern() != null,
      ctx.local_struct().accept(this).asInstanceOf[FcLocalStruct]
    )

  override def visitLocal_struct(ctx: FunCParser.Local_structContext): FcNode =
    FcLocalStruct(
      getId(ctx.id()),
      ctx.fun_single_args().accept(this).getAggregate.map(_.asInstanceOf[FcVal])
    )


}
