package parser

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import org.scalatest.{FlatSpec, Matchers}
import parser.tree._
import utils.view.ViewFcNode

class GenTreeSpec extends FlatSpec with Matchers {

  def save(file: String, content: String): Unit =
    Files.write(Paths.get("src/test/resources/out/" + file), content.getBytes(StandardCharsets.US_ASCII))

  val parser: Parser = Parser.fromFile("src/test/resources/code/first.fc")
  val tree: FcSource = parser.tree
  val code: String = parser.code

  it should "parse code" in {
    println(code)
    println(tree.toString)
    println(ViewFcNode.treeString(tree, "first.fc", 0))
    save("first.json", ViewFcNode.toJson(tree))
  }

}
