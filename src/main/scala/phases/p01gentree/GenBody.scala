package phases.p01gentree

import fun_c.FunCParser
import parser.tree._

trait GenBody {
  this: GenTreeVisitor =>

  def gotoBody(ctx: FunCParser.BodyContext): List[FcBodyStatement] =
    if (ctx == null) List[FcBodyStatement]()
    else ctx.accept(this).getAggregate.map(_.asInstanceOf[FcBodyStatement])

  override def visitC_block(ctx: FunCParser.C_blockContext): FcNode =
    FcCBlock(ctx.C_BODY().getText)

  override def visitBody(ctx: FunCParser.BodyContext): FcNode = {
    val first = ctx.body_statement().accept(this)
    val rest = ctx.body()
    if (rest == null) {
      FcAggregate(List(first))
    } else {
      joinNodes(first, rest.accept(this))
    }
  }

  override def visitLocal_namespace(ctx: FunCParser.Local_namespaceContext): FcNode =
    FcLocalNamespace(
      getId(ctx.id()),
      gotoBody(ctx.body())
    )

  override def visitStatic_val(ctx: FunCParser.Static_valContext): FcNode = {
    val declaredType = getId(ctx.declare_val().type_id().id())
    val declaredName = getId(ctx.declare_val().id())
    val assign = getExpr(ctx.declare_val().expression())
    FcStaticVal(
      ctx.extern() != null,
      ctx.kwlazy() != null,
      FcVal(declaredType, declaredName),
      assign
    )
  }

  override def visitDeclare_struct(ctx: FunCParser.Declare_structContext): FcNode =
    FcStruct(
      ctx.extern() != null,
      getId(ctx.id()),
      ctx.fun_single_args().accept(this).getAggregate.map(_.asInstanceOf[FcVal])
    )

}
