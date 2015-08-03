name := "scala-storm-starter"

version := "0.0.2-SNAPSHOT"

scalaVersion := "2.11.7"

fork in run := true

resolvers ++= Seq(
  "twitter4j" at "http://twitter4j.org/maven2",
  "clojars.org" at "http://clojars.org/repo"
)

libraryDependencies ++= Seq(
  "org.apache.storm" % "storm-core" % "0.9.3",
  "org.clojure" % "clojure" % "1.4.0" % "provided",
  "org.twitter4j" % "twitter4j-core" % "4.0.2",
  "org.twitter4j" % "twitter4j-stream" % "4.0.2"
)

mainClass in Compile := Some("Main")

mainClass in assembly := Some("Main")

TaskKey[File]("generate-storm") <<= (baseDirectory, fullClasspath in Compile, mainClass in Compile) map { (base, cp, main) =>
  val template = """#!/bin/sh
java -classpath "%s" %s "$@"
"""
  val mainStr = main getOrElse error("No main class specified")
  val contents = template.format(cp.files.absString, mainStr)
  val out = base / "bin/run-main-topology.sh"
  IO.write(out, contents)
  out.setExecutable(true)
  out
}
