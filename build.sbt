name := "connect four"
organization in ThisBuild := "connect four in scala"
scalaVersion in ThisBuild := "2.12.7"

//PROJECTS
val meta = """META.INF(.)*""".r

lazy val global = project.in(file("."))
  .settings(
    libraryDependencies ++= mainModuleDependencies,
    assemblyMergeStrategy in assembly := {
      case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
      case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
      case n if n.startsWith("reference.conf") => MergeStrategy.concat
      case n if n.endsWith(".conf") => MergeStrategy.concat
      case meta(_) => MergeStrategy.discard
      case x => MergeStrategy.first
    }
  )
  .aggregate(
    controlModule,
    playerModule,
    modelModule
  )
  //.enablePlugins(GatlingPlugin)

lazy val controlModule = project
  .settings(name := "ControlModule",
    libraryDependencies ++= mainModuleDependencies,
    assemblyMergeStrategy in assembly := {
      case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
      case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
      case n if n.startsWith("reference.conf") => MergeStrategy.concat
      case n if n.endsWith(".conf") => MergeStrategy.concat
      case meta(_) => MergeStrategy.discard
      case x => MergeStrategy.first
    },
    mainClass in assembly := Some("aview/ConnectFour") ,
//    mainClass in (Compile, run) := Some("ConnectFour")
  )
  //.enablePlugins(GatlingPlugin)
  .dependsOn(playerModule,modelModule)
  .aggregate(playerModule,modelModule)

lazy val playerModule = project
  .settings(name := "PlayerModule",
    libraryDependencies ++= mainModuleDependencies,
    assemblyMergeStrategy in assembly := {
      case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
      case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
      case n if n.startsWith("reference.conf") => MergeStrategy.concat
      case n if n.endsWith(".conf") => MergeStrategy.concat
      case meta(_) => MergeStrategy.discard
      case x => MergeStrategy.first
    }
    ,
    mainClass in assembly := Some("aview/PlayerMain"),
//    mainClass in (Compile, run) := Some("aview/PlayerMain")
  )


lazy val modelModule = project
  .settings(name := "ModelModule",
    libraryDependencies ++= mainModuleDependencies,
    assemblyMergeStrategy in assembly := {
      case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
      case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
      case n if n.startsWith("reference.conf") => MergeStrategy.concat
      case n if n.endsWith(".conf") => MergeStrategy.concat
      case meta(_) => MergeStrategy.discard
      case x => MergeStrategy.first
    },
    mainClass in assembly := Some("aview/GridMain"),
//    mainClass in (Compile, run) := Some("aview/GridMain")
  )
  .dependsOn(playerModule)

// DEPENDENCIES
lazy val dependencies =
  new {
    val logbackV = "1.2.3"
    val logstashV = "4.11"
    val scalaLoggingV = "3.7.2"
    val slf4jV = "1.7.25"
    val typesafeConfigV = "1.3.1"
    val pureconfigV = "0.8.0"
    val monocleV = "1.4.0"
    val akkaV = "2.5.6"
    val scalatestV = "3.0.4"
    val scalacheckV = "1.13.5"

    val logback = "ch.qos.logback" % "logback-classic" % logbackV
    val logstash = "net.logstash.logback" % "logstash-logback-encoder" % logstashV
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingV
    val slf4j = "org.slf4j" % "jcl-over-slf4j" % slf4jV
    val typesafeConfig = "com.typesafe" % "config" % typesafeConfigV
    val akka = "com.typesafe.akka" %% "akka-stream" % akkaV
    val monocleCore = "com.github.julien-truffaut" %% "monocle-core" % monocleV
    val monocleMacro = "com.github.julien-truffaut" %% "monocle-macro" % monocleV
    val pureconfig = "com.github.pureconfig" %% "pureconfig" % pureconfigV
    val scalatest = "org.scalatest" %% "scalatest" % scalatestV
    val scalacheck = "org.scalacheck" %% "scalacheck" % scalacheckV
    val gguice = "com.google.inject" % "guice" % "4.1.0"
  }

lazy val mainModuleDependencies = Seq(
  dependencies.logback,
  dependencies.logstash,
  dependencies.scalaLogging,
  dependencies.slf4j,
  dependencies.typesafeConfig,
  dependencies.akka,
  dependencies.gguice,
  dependencies.scalatest % "test",
  dependencies.scalacheck % "test",
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "com.google.inject" % "guice" % "4.1.0",
  "net.codingwell" %% "scala-guice" % "4.1.0",
  "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6",
  "com.typesafe.play" %% "play-json" % "2.6.6",
  "org.scala-lang.modules" % "scala-swing_2.12" % "2.0.3",
  "org.scalafx" %% "scalafx" % "11-R16",
  "com.typesafe.akka" %% "akka-http" % "10.0.7",
  "com.typesafe.slick" %% "slick" % "3.3.2",
  "com.h2database" % "h2" % "1.4.199",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.6.0",
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.3.1" % "test",
  "io.gatling"            % "gatling-test-framework"    % "3.3.1" % "test"
)

coverageExcludedPackages := ".*gui.*"