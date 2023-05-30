ThisBuild / scalaVersion := "3.2.0"

lazy val webpage = project
  .in(file("webpage"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.3.0"
    )
  )
  .dependsOn(core.js)

lazy val webserver = project
  .in(file("webserver"))
  .settings(
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "cask" % "0.9.0",
      "org.scalameta" %% "munit" % "1.0.0-M3" % Test
    ),
    Compile / resourceGenerators += Def.task {
      val source = (webpage / Compile / scalaJSLinkedFile).value.data
      val dest = (Compile / resourceManaged).value / "assets" / "main.js"
      IO.copy(Seq(source -> dest))
      Seq(dest)
    },
    run / fork := true
  )
  .dependsOn(core.jvm)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .settings(
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "upickle" % "3.0.0",
      "org.scalameta" %%% "munit" % "1.0.0-M3" % Test,
      "com.softwaremill.sttp.client3" %% "core" % "3.8.15",
      "com.softwaremill.sttp.client3" %% "circe" % "3.8.15",
      "io.circe" %% "circe-core" % "0.14.5",
      "io.circe" %% "circe-parser" % "0.14.5",
      "io.circe" %% "circe-generic" % "0.14.5",
      "junit" % "junit" % "4.13.2",
      "org.typelevel" %% "cats-effect" % "3.5.0",
      "org.typelevel" %% "munit-cats-effect-3" % "1.0.7"
      //"io.circe" %% "circe-generic-extras" % "0.14.5"
    )
  )
