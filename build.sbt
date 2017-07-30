name := "ier-frontend"
organization := "uk.gov.ier"

version := "1.0-SNAPSHOT"

resolvers += "Madoushi sbt-plugins" at "https://dl.bintray.com/madoushi/sbt-plugins/"

lazy val root = (project in file(".")).enablePlugins(PlayScala).enablePlugins(SbtTwirl)

scalaVersion := "2.11.11"

libraryDependencies += filters
libraryDependencies += ws
libraryDependencies += "com.github.spullara.mustache.java" % "scala-extensions-2.11" % "0.9.4"
libraryDependencies += "commons-lang" % "commons-lang" % "2.6"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.8"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test
//libraryDependencies += "com.google.inject" % "guice" % "3.0"
//libraryDependencies +=  "uk.gov.gds" %% "gds-scala-utils" % "0.7.6-SNAPSHOT" exclude("com.google.code.findbugs", "jsr305")
libraryDependencies += "joda-time" % "joda-time" % "2.1"
libraryDependencies += "com.kenshoo" %% "metrics-play" % "2.4.0_0.4.1"
libraryDependencies += "com.codahale.metrics" % "metrics-graphite" % "3.0.1"
libraryDependencies += new ModuleID("org.codehaus.janino", "janino", "2.6.1")
libraryDependencies += "org.mockito" % "mockito-core" % "1.9.5"
//"org.jba" %% "play2-mustache" % "1.1.3", // play2.2.0
libraryDependencies += "org.jsoup" % "jsoup" % "1.7.2"
//libraryDependencies +=  "com.typesafe.play.plugins" %% "play-statsd" % "2.3.0"
libraryDependencies += "org.julienrf" %% "play-jsmessages" % "2.0.0"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"

//managedResourceDirectories in Assets += baseDirectory.value / "app" / "assets" / "mustache" / "govuk_template_inheritance" / "assets"