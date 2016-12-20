name := "VendingMachine"

version := "1.0"

scalaVersion := "2.12.1"

organization  := "App with Config "

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= {
  val akkaV = "10.0.0"
  val scalaTestV = "3.0.1"

  Seq(
    "com.typesafe.akka" %% "akka-http" % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaV % "test",
    "org.scalatest" %% "scalatest" % scalaTestV % "test",
    "ch.qos.logback" % "logback-classic" % "1.1.3"
  )

}

Revolver.settings
enablePlugins(JavaAppPackaging)

    