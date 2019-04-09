package parser.tree

sealed abstract class FcFunStatement extends FcNode { this: Product => }

case class FcLocalFunc(ftype: FcId, name: FcId, args: List[FcVal],
                  vargs: Option[FcVal], body: List[FcFunStatement]) extends FcFunStatement

case class FcLocalVal(isLazy: Boolean, declare: FcVal, assign: FcExprBlock) extends FcFunStatement

case class FcLocalStruct(id: FcId, vals: List[FcVal]) extends FcFunStatement

case class FcExprBlock(block: List[FcFunStatement]) extends FcFunStatement

sealed abstract class FcExpr extends FcFunStatement { this: Product => }

case class FcGetVal(path: List[FcId]) extends FcExpr

case class FcCallFun(path: List[FcId], args: List[FcExpr]) extends FcExpr

case class FcValue(value: FcLiteral) extends FcExpr

case class FcPrefixExpr(pref: String, expr: FcExpr) extends FcExpr

case class FcExprOpExpr(exp1: FcExpr, op: String, exp2: FcExpr) extends FcExpr

case class FcIfExpr(condition: FcExpr, ifexp: FcExprBlock, elsexp: FcExprBlock) extends FcExpr


case class FcLambda(args: List[FcLambdaArg], body: FcExprBlock) extends FcExpr
sealed abstract class FcLambdaArg extends FcNode { this: Product => }
case class FcLambdaArgId(id: FcId) extends FcLambdaArg
case object FcLambdaArgAny extends FcLambdaArg


case class FcMatch(expr: FcExpr, cases: List[FcCase]) extends FcExpr
sealed abstract class FcCase extends FcNode { this: Product => }
case class FcCasePat(patterns: List[FcExpr], result: FcExprBlock) extends FcCase
case class FcCaseAny(result: FcExprBlock) extends FcCase
