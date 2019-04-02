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

}
