name := "test"

version := "1.0"

scalaVersion := "2.10.5"

libraryDependencies ++= {
  val akkaV = "2.3.12"
  val sprayV = "1.3.3"
  Seq(
    "io.spray" %% "spray-can" % sprayV,
    "io.spray" %% "spray-routing" % sprayV,
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-remote" % akkaV,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "ch.qos.logback" % "logback-classic" % "1.0.9",
    "com.typesafe.play" %% "play-json" % "2.4.1",
    "commons-io" % "commons-io" % "2.4",
    "org.scalaz" %% "scalaz-core" % "7.1.3",
    "net.databinder.dispatch" %% "dispatch-core" % "0.11.2"
  )
}
    