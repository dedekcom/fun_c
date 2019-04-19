package model

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import org.scalatest.{FlatSpec, Matchers}
import model.tree._
import utils.Parser
import utils.view.ViewFcNode

class GenTreeSpec extends FlatSpec with Matchers {

  def save(file: String, content: String): Unit =
    Files.write(Paths.get("src/test/resources/out/" + file), content.getBytes(StandardCharsets.US_ASCII))

  val code1: TestCode = load("first.fc")
  val code2: TestCode = load("third.fc")

  case class TestCode(tree: FcSource, code: String)

  it should "parse code" in {
    //println(code)
    //println(tree.toString)
    save("first.txt", ViewFcNode.treeString(code1.tree, "first.fc", 0))
    save("first.json", ViewFcNode.toJson(code1.tree))
    save("second.json", ViewFcNode.toJson(code2.tree))
  }

  def load(filename: String): TestCode = {
    val parser: Parser = Parser.fromFile("src/test/resources/code/parser_test/" + filename)
    val tree: FcSource = parser.source
    val code: String = parser.code
    TestCode(tree, code)
  }

}
