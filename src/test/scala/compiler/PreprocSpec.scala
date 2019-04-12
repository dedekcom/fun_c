package compiler

import org.scalatest.{FlatSpec, Matchers}
import out.messages._
import model.tree._
import model.tree.TokenMeta.EmptyToken
import phases.p01preproc.CompilerRunner

class PreprocSpec extends FlatSpec with Matchers {


  it should "load files" in {
    val preproc = new CompilerRunner("src/test/resources/code/include_test", "main.fc")
    val x = 5
  }

  an [java.io.FileNotFoundException] should be thrownBy {
    val preproc = new CompilerRunner("src/test/resources/code/include_test", "main2.fc")
  }

  the [CompileException] thrownBy {
    val preproc = new CompilerRunner("src/test/resources/code/include_test", "main3.fc")
  } should have message ErrorIncludeTooShort(FcId("wrong_namespace", EmptyToken)).getMessage

  the [CompileException] thrownBy {
    val preproc = new CompilerRunner("src/test/resources/code/include_test", "main4.fc")
  } should have message ErrorNamespaceWrongName(FcId("A", EmptyToken), "Wrong_namespace_name").getMessage

}
