package phases.p02gentree

import fun_c.FunCParser
import parser.tree._
import scala.collection.JavaConverters._

trait GenFun {
  this: GenTreeVisitor =>

  def getType(ctx: FunCParser.Type_idContext): FcType = ctx.accept(this).asInstanceOf[FcType]

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
      getType(ctx.type_id()),
      getId(ctx.id()),
      fargs,
      fvarargs,
      ctx.fun_block().accept(this).getAggregate.map(_.asInstanceOf[FcFunStatement])
    )
  }

  override def visitTyp_id(ctx: FunCParser.Typ_idContext): FcNode =
    FcType(getId(ctx.id()), None)

  override def visitTyp_templ(ctx: FunCParser.Typ_templContext): FcNode =
    FcType(getId(ctx.id()), Some(getType(ctx.type_id())))

  override def visitFun_block(ctx: FunCParser.Fun_blockContext): FcNode =
    FcAggregate(ctx.fun_body().asScala.toList.map(_.accept(this).asInstanceOf[FcFunStatement]))

  override def visitDeclare_func(ctx: FunCParser.Declare_funcContext): FcNode = FcFunc(ctx.extern() != null, declareFunc(ctx.local_func()))

  override def visitLocal_func(ctx: FunCParser.Local_funcContext): FcNode = declareFunc(ctx)

  override def visitDeclare_val(ctx: FunCParser.Declare_valContext): FcNode = {
    val declaredType = getType(ctx.type_id())
    val declaredName = getId(ctx.id())
    FcLocalVal(
      ctx.kwlazy() != null,
      FcVal(declaredType, declaredName),
      getExprBlock(ctx.expr_block())
    )
  }

  override def visitFun_body(ctx: FunCParser.Fun_bodyContext): FcNode = {
    if (ctx.declare_val() != null) FcExprBlock(List(ctx.declare_val().accept(this).asInstanceOf[FcLocalVal]))
    else if (ctx.local_func() != null) FcExprBlock(List(ctx.local_func().accept(this).asInstanceOf[FcLocalFunc]))
    else if (ctx.local_struct() != null) FcExprBlock(List(ctx.local_struct().accept(this).asInstanceOf[FcLocalStruct]))
    else getExprBlock(ctx.expr_block())
  }

}
