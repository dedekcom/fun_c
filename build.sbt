name := "fun_c"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.antlr" % "antlr4" % "4.7.2",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)

enablePlugins(Antlr4Plugin)
antlr4Version in Antlr4 := "4.7.2" // default: 4.7.2
antlr4GenListener in Antlr4 := false // default: true
antlr4GenVisitor in Antlr4 := true // default: false
antlr4TreatWarningsAsErrors in Antlr4 := true // default: false
antlr4PackageName in Antlr4 := Some("fun_c")
javaSource in Antlr4        := (javaSource in Compile).value
sourceDirectory in Antlr4   := baseDirectory.value / "src" / "main" / "antlr4"
