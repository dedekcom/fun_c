package phases.p01gentree

import fun_c.FunCParser
import parser.tree._
import scala.collection.JavaConverters._

trait GenFun {
  this: GenTreeVisitor =>

  def declareFunc(ctx: FunCParser.Local_funcContext): FcLocalFunc = {
    val (fargs, fvarargs) = if (ctx.fun_args() == null) (List[FcVal](), Option.empty[FcVal])
    else {
      val fa = if (ctx.fun_args().fun_single_args() != null)
        ctx.fun_args().fun_single_args().accept(this).getAggregate.map(_.asInstanceOf[FcVal])
      else List[FcVal]()
      val fv = if (ctx.fun_args().fun_varargs() != null)
        Some(ctx.fun_args().fun_varargs().accept(this).asInstanceOf[FcVal])
      else Option.empty[FcVal]
      (fa, fv)
    }
    FcLocalFunc(
      getId(ctx.type_id().id()),
      getId(ctx.id()),
      fargs,
      fvarargs,
      ctx.fun_block().accept(this).getAggregate.map(_.asInstanceOf[FcFunStatement])
    )
  }

  override def visitFun_block(ctx: FunCParser.Fun_blockContext): FcNode =
    FcAggregate(ctx.fun_body().asScala.toList.map(_.accept(this).asInstanceOf[FcFunStatement]))

  override def visitDeclare_func(ctx: FunCParser.Declare_funcContext): FcNode = FcFunc(ctx.extern() != null, declareFunc(ctx.local_func()))

  override def visitLocal_func(ctx: FunCParser.Local_funcContext): FcNode = declareFunc(ctx)
}
