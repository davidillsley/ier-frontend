// Comment to get more information during initialization
logLevel := Level.Warn

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.14")
//addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.3.0")

resolvers += "Madoushi sbt-plugins" at "https://dl.bintray.com/madoushi/sbt-plugins/"

addSbtPlugin("org.madoushi.sbt" % "sbt-sass" % "0.9.3")

libraryDependencies ++= Seq(
	"com.kenshoo" % "metrics-play_2.10" % "0.1.2",
	"com.codahale.metrics" % "metrics-graphite" % "3.0.1")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.3.2")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.4")
