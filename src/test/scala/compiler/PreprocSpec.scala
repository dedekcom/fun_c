package compiler

import org.scalatest.{FlatSpec, Matchers}
import phases.p01preproc.CompilerRunner

class PreprocSpec extends FlatSpec with Matchers {

  it should "load files" in {
    val preproc = new CompilerRunner("src/test/resources/code/include_test", "main.fc")
    val x = 5
  }

}
