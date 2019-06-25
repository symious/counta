name := "counta"

version := "0.1"

scalaVersion := "2.11.8"


val sparkVersion = "2.3.2"
val hadoopVersion = "2.6.4"
val awsJavaSDKVersion = "1.11.22"


libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time" % "2.16.0",

  "org.apache.spark" %% "spark-core" % sparkVersion % "provided" exclude("org.apache.hadoop", "hadoop-client"),
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided" exclude("org.apache.hadoop", "hadoop-client"),

  "com.databricks" %% "spark-redshift" % "3.0.0-preview1",
  "com.amazon.redshift" % "jdbc41" % "1.2.1.1001" from "https://s3.amazonaws.com/redshift-downloads/drivers/RedshiftJDBC41-1.2.1.1001.jar",
  "org.apache.kafka" %% "kafka" % "1.1.0",

  "com.typesafe.akka" %% "akka-actor" % "2.5.23",


"com.typesafe.akka" %% "akka-testkit" % "2.5.23" % Test


)

dependencyOverrides += "com.databricks" % "spark-avro_2.11" % "3.2.0"

libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-client" % hadoopVersion % "provided" exclude("javax.servlet", "servlet-api") force(),
  "org.apache.hadoop" % "hadoop-common" % hadoopVersion % "provided" exclude("javax.servlet", "servlet-api") force()
)

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-core" % awsJavaSDKVersion exclude("com.fasterxml.jackson.core", "jackson-databind"),
  "com.amazonaws" % "aws-java-sdk-s3" % awsJavaSDKVersion exclude("com.fasterxml.jackson.core", "jackson-databind")
)


