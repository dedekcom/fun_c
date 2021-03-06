package utils.view

import model.tree.{FcCBlock, FcNode, TokenMeta}

object ViewFcNode {

  def treeString(obj: Any, objName: String, depth: Int): String = {
    val margin = " "
    val padding = margin * depth

    def extractFields: String = obj.getClass.getDeclaredFields.
      filterNot(_.getName.startsWith("$")).
      map { f =>
        f.setAccessible(true)
        val value = f.get(obj)
        if (value == null) (None, "")
        else (Some(value), f.getName)
      }.map {
      case (Some(l: List[_]), name) =>
        val pad = margin * (depth + 1)
        s"${pad}List.$name [\n" + l.map(o => treeString(o, "", depth + 2)).mkString + s"$pad]\n"
      case (Some(n), name) => treeString(n, name, depth + 1)
      case (None, _) => ""
    }.mkString

    obj match {
      case n: FcNode =>
        val name = if (objName == "") "" else "." + objName
        s"$padding${n.caseClassName}$name:\n" + extractFields

      case TokenMeta(_, line, range) => s"$padding$objName($line,${range.start},${range.end})\n"

      case rest => s"$padding$objName($rest)\n"
    }
  }

  def toJson(obj: Any, objName: String = "", depth: Int = 0): String = {
    val margin = " "
    val padding = margin * depth

    def putBody(pad: String, typename: String, body: String): String = pad + "\"type\": \"" +
      typename + (if(body.isEmpty) "\"" else "\",\n" + body)

    def extractFields: String = obj.getClass.getDeclaredFields.
      filterNot(_.getName.startsWith("$")).
      map { f =>
        f.setAccessible(true)
        val value = f.get(obj)
        if (value == null) (None, "")
        else (Some(value), f.getName)
      }.filter(_._1.nonEmpty).
      map {
        case p @ (Some(_: List[_] | _: Option[_]), name) =>
          val l = p._1 match {
            case Some(ll: List[_]) => ll
            case Some(oo: Option[_]) => oo.toList
            case _ => List()
          }
          val pad = margin * (depth + 1)
          pad + "\"" + name + "\": [\n" + l.map(o => toJson(o, "", depth + 2)).mkString(",\n") + s"\n$pad]"

        case (Some(n), name) => toJson(n, name, depth + 1)

        case (None, _) => ""
    }.mkString(",\n")

    obj match {
      case b @ FcCBlock(block) =>
        val pad = padding + margin
        val lines = s"{\n$pad" + "\"type\": \"" + b.caseClassName + "\",\n" +
          pad + "\"block\": [\n" +
          block.replaceAllLiterally("\r\n", "\n").
          split('\n').map(line => pad + margin + "\"" + line + "\"").
          mkString(",\n") + "\n" + pad + "]\n" + padding + "}"
        if (objName == "")
          padding + lines
        else
          padding + "\"" + objName + "\": " + lines

      case n: FcNode =>
        if (objName == "")
          padding + "{\n" + putBody(padding + margin, n.caseClassName, extractFields) + "\n" + padding + "}"
        else
          padding + "\"" + objName + "\": {\n" + putBody(padding + margin, n.caseClassName, extractFields) + "\n" + padding + "}"

      case TokenMeta(filename, line, range) => padding + "\"" + objName + "\": " + "[\"" + filename +
        "\"" + s", $line, ${range.start}, ${range.end}]"

      case _: Int => padding + "\"" + objName + "\": " + obj.toString
      case _: Float => padding + "\"" + objName + "\": " + obj.toString
      case _: Double => padding + "\"" + objName + "\": " + obj.toString

      case rest => padding + "\"" + objName + "\": \"" + rest + "\""
    }
  }

}
