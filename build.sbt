import ReleaseTransformations._

name := "pg2bq"

scalaVersion := "2.11.11"

scalafmtOnCompile in ThisBuild := true
enablePlugins(JavaAppPackaging)
enablePlugins(LauncherJarPlugin)

// you shall not pass
scalacOptions in ThisBuild := Seq(
  "-unchecked",
  "-feature",
  "-deprecation",
  "-encoding",
  "utf8",
  "-unchecked",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused",
  "-Ywarn-unused-import"
)

libraryDependencies += "com.databricks" %% "spark-avro" % "4.0.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.2.0",
  "org.apache.spark" %% "spark-sql" % "2.2.0"
)

libraryDependencies += "com.google.cloud" % "google-cloud-bigquery" % "0.26.0-beta"
libraryDependencies += "com.google.cloud" % "google-cloud-storage" % "1.10.0"
libraryDependencies += "com.google.cloud.bigdataoss" % "gcs-connector" % "1.6.1-hadoop2"
//libraryDependencies += "com.spotify" % "spark-bigquery_2.11" % "0.2.1"
libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.8.0"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  //publishArtifacts,
  setNextVersion,
  commitNextVersion,
  pushChanges
)
