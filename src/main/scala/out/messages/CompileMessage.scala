package out.messages

import parser.tree.FcId

abstract class CompileMessage(val tokenId: Option[FcId]) {
  def getMessage: String
}

abstract class CompileError(tokenId: Option[FcId]) extends CompileMessage(tokenId)

case class ErrorIncludeTooShort(id: FcId) extends CompileError(Some(id)) {
  override def getMessage: String = s"include ${id.id} must contain path and namespace"
}

case class ErrorNamespaceWrongName(id: FcId, expected: String) extends CompileError(Some(id)) {
  override def getMessage: String = s"wrong namespace: ${id.id} ; expected: $expected"
}