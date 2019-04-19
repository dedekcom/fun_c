package phases.p01preproc

import CompilerRunner._
import out.messages.{CompileException, ErrorIncludeTooShort, ErrorNamespaceWrongName}
import model.tree._
import utils.Parser

object CompilerRunner {

  val NamespaceMain = "Main"

}

class CompilerRunner(prjPath: String, mainFile: String) {

  val documents: Map[String, Parser] = run()

  private def run(): Map[String, Parser] = {
    def loop(file: String, namespace: String, namespaces: Map[String, Parser]): Map[String, Parser] = {
      loadNamespace(file, namespace, namespaces) match {
        case (result, None) => result

        case (result, Some(parser)) =>

          parser.source.includes.foldLeft(result) {
            case (nspcs, include: FcInclude) =>
              if (include.path.length < 2)
                throw CompileException(ErrorIncludeTooShort(include.path.head))
              loop(include.getFile, include.fullNamespace, nspcs)
          }
      }
    }
    loop(mainFile, NamespaceMain, Map[String, Parser]())
  }

  private def loadNamespace(file: String, namespacePath: String, namespaces: Map[String, Parser]): (Map[String, Parser], Option[Parser]) =
    if (namespaces.contains(namespacePath)) (namespaces, None)
    else {
      val parser: Parser = Parser.fromFile(s"$prjPath/$file")
      val loadedNs = parser.source.namespace.path.last
      val namespace = namespacePath.split('.').last
      if (loadedNs.id != namespace)
        throw CompileException(ErrorNamespaceWrongName(loadedNs, namespace))
      (namespaces + (namespacePath -> parser), Some(parser))
    }

}
