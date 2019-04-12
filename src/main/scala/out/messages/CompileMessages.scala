package out.messages

object CompileMessages {

  val EmptyMessages = CompileMessages(Nil, Nil, Nil)

  def apply(): CompileMessages = EmptyMessages

}

case class CompileMessages(infos: List[CompileMessage], warnings: List[CompileMessage], errors: List[CompileMessage]) {

  def +(that: CompileMessages): CompileMessages = CompileMessages(that.infos ::: infos, that.warnings ::: warnings, that.errors ::: errors)

  def +(info: CompileInfo): CompileMessages = copy(infos = info :: infos)

  def +(warning: CompileWarning): CompileMessages = copy(warnings = warning :: warnings)

  def +(error: CompileError): CompileMessages = copy(errors = error :: errors)

}
