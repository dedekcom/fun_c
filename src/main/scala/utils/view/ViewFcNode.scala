package utils.view

import parser.tree.FcNode

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
      case rest => s"$padding$objName($rest)\n"
    }
  }

  def toJson(obj: Any, objName: String, depth: Int): String = {
    val margin = " "
    val padding = margin * depth

    def extractFields: String = obj.getClass.getDeclaredFields.
      filterNot(_.getName.startsWith("$")).
      map { f =>
        f.setAccessible(true)
        val value = f.get(obj)
        if (value == null) (None, "")
        else (Some(value), f.getName)
      }.filter(_._1.nonEmpty).
      map {
      case (Some(l: List[_]), name) =>
        val pad = margin * (depth + 1)
        pad + "\"" + name + "\": [\n" + l.map(o => toJson(o, "", depth + 2)).mkString(",\n") + s"\n$pad]"
      case (Some(n), name) => toJson(n, name, depth + 1)
      case (None, _) => ""
    }.mkString(",\n")

    obj match {
      case n: FcNode =>
        if (objName == "")
          padding + "{\n" + extractFields + "\n" + padding + "}"
        else
          padding + "\"" + objName + "\": {\n" + extractFields + "\n" + padding + "}"
      case _: Int | _: Double | _: Float => padding + "\"" + objName + "\": " + obj.toString
      case rest => padding + "\"" + objName + "\": \"" + rest + "\""
    }
  }

}
