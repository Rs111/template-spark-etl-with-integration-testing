
name := "template-spark-etl-with-integration-testing"

version := "0.0.1"

scalaVersion := "2.12.12"

val flipplib = "https://flipplib.jfrog.io/flipplib/"

resolvers ++= Seq(
  Resolver.mavenLocal,
  Resolver.jcenterRepo,
  Resolver.sonatypeRepo("releases"),
  "Flipplib Ext-Releases-Local" at flipplib + "ext-release-local",
  "FlippLib Releases" at flipplib + "libs-release-local",
  "FlippLib Snapshots" at flipplib + "libs-snapshot-local",
  "confluent" at "https://packages.confluent.io/maven/",
  "jitpack" at "https://jitpack.io"
)

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "3.5.0",
  "org.apache.spark" %% "spark-sql" % "3.0.0" % "provided",
  "org.clapper" %% "grizzled-slf4j" % "1.3.1",
  "io.findify" %% "s3mock" % "0.2.6" % Test,
  "org.scalatest" %% "scalatest" % "3.2.0" % Test,
//  "io.delta" %% "delta-core" % "0.7.0",
  "org.apache.hadoop" % "hadoop-aws" % "3.3.0" % Test,
  "org.apache.hadoop" % "hadoop-common" % "3.3.0" % Test excludeAll "com.sun.jersey",
  "org.apache.hadoop" % "hadoop-client" % "3.3.0" % Test excludeAll "com.sun.jersey",
)