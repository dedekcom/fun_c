package compiler

import org.scalatest.{FlatSpec, Matchers}
import phases.p01preproc.CompilerRunner
import phases.p03scopes.GenScopes

class GenScopesSpec extends FlatSpec with Matchers {

  it should "gen scopes" in {
    val preproc = new CompilerRunner("src/test/resources/code/scopes/simple1", "simple1.fc")
    val scopes = new GenScopes(preproc)
    val x = 3
  }

}
