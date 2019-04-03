package parser.tree

case class FcFunc(isExtern: Boolean, args: List[FcVal], body: List[FcFunStatement]) extends FcBodyStatement

abstract class FcFunStatement extends FcNode { this: Product => }

case class FcLocalVal(declare: FcVal, assign: FcExpr) extends FcFunStatement

abstract class FcExpr extends FcFunStatement { this: Product => }

case class FcGetVal(path: List[FcId]) extends FcExpr

case class FcCallFun(path: List[FcId], args: List[FcExpr]) extends FcExpr

case class FcValue(value: FcLiteral) extends FcExpr

case class FcPrefixExpr(pref: String, expr: FcExpr) extends FcExpr

case class FcExprOpExpr(exp1: FcExpr, op: String, exp2: FcExpr) extends FcExpr

case class FcIfExpr(ifexp: FcExpr, elsexp: FcExpr) extends FcExpr

case class FcLambda(args: List[FcLambdaArg], body: List[FcFunStatement]) extends FcExpr
abstract class FcLambdaArg extends FcNode { this: Product => }
case class FcLambdaArgId(id: FcId) extends FcLambdaArg
case object FcLambdaArgAny extends FcLambdaArg

case class FcMatch(cases: List[FcCase]) extends FcExpr
case class FcCase(patterns: List[FcExpr], result: FcExpr) extends FcNode
