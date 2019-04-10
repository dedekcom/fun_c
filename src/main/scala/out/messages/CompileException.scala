package out.messages

case class CompileException(message: CompileMessage) extends RuntimeException(message.getMessage)
