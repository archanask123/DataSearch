name := "JSONDataSearch"

version := "0.1"

scalaVersion := "2.11.8"

val sparkVersion = "2.2.1"

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.11" % sparkVersion,
  "org.apache.spark" % "spark-sql_2.11" % sparkVersion,
  "org.apache.spark" % "spark-streaming_2.11" % sparkVersion,
  "org.apache.spark" % "spark-mllib_2.11" % sparkVersion,
  "org.jmockit" % "jmockit" % "1.34" % "test"
)

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"
//
// addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.3")

