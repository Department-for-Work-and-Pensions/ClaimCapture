import com.typesafe.sbt.web.SbtWeb
import sbt._
import sbt.Keys._
import play.sbt.Play.autoImport._
import play.sbt.PlayImport._
import de.johoop.jacoco4sbt.JacocoPlugin._

object ApplicationBuild extends Build {
  val appName         = "c3"


  val appVersion      = "3.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    "com.typesafe.play" %% "play-cache" % "2.4.3",
    ws,
    "com.typesafe.play"  %% "anorm"               % "2.4.0",
    "org.mockito"         % "mockito-all"         % "1.10.19" % "test" withSources() withJavadoc(),
    "com.typesafe.akka"  %% "akka-testkit"        % "2.3.9" % "test" withSources() withJavadoc(),
    "com.typesafe.akka"  %% "akka-agent"          % "2.3.9" % "test" withSources() withJavadoc(),
    "com.typesafe.akka"  %% "akka-remote"         % "2.3.9" % "test" withSources() withJavadoc(),
    "gov.dwp.carers"     %% "xmlcommons"          % "7.0",
    "gov.dwp.carers"     %%  "wscommons"          % "3.0",
    "org.postgresql"     % "postgresql"           % "9.3-1103-jdbc41",
    "com.h2database"      % "h2"                  % "1.4.186"  % "test",
    "me.moocar"           % "logback-gelf"        % "0.12",
    "com.github.rjeschke" % "txtmark"             % "0.11",
    "org.jacoco"          % "org.jacoco.core"     % "0.7.4.201502262128"  % "test",
    "org.jacoco"          % "org.jacoco.report"   % "0.7.4.201502262128"  % "test",
    "nl.rhinofly"        %% "play-mailer"         % "3.0.0",
    "gov.dwp.carers"     %% "play2-resilient-memcached"     % "2.1",
    "gov.dwp"            %% "play2-multimessages" % "2.4.3",
    "net.sourceforge.htmlunit" % "htmlunit" % "2.18" % "test",
    "org.seleniumhq.selenium" % "selenium-java" % "2.47.1" % "test",
    "org.apache.httpcomponents" % "httpclient" % "4.5" ,
    "org.apache.httpcomponents" % "httpcore" % "4.4.1",
    "commons-io" % "commons-io" % "2.4",
    "org.specs2" %% "specs2-core" % "3.3.1" % "test" withSources() withJavadoc(),
    "org.specs2" %% "specs2-mock" % "3.3.1" % "test" withSources() withJavadoc(),
    "org.specs2" %% "specs2-junit" % "3.3.1" % "test" withSources() withJavadoc(),
    "com.kenshoo" % "metrics-play_2.10" % "2.4.0_0.4.0"
  )

  var sO: Seq[Def.Setting[_]] = Seq(scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-Xlint", "-language:reflectiveCalls"))

  var sV: Seq[Def.Setting[_]] = Seq(scalaVersion := "2.10.5")

  var sR: Seq[Def.Setting[_]] = Seq(
    resolvers += "Carers repo" at "http://build.3cbeta.co.uk:8080/artifactory/repo/",
    resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases",
    resolvers += "Rhinofly Internal Release Repository" at "http://maven-repository.rhinofly.net:8081/artifactory/libs-release-local",
    resolvers += "Scalaz Bintray Repo"  at "http://dl.bintray.com/scalaz/releases")


  var sTest: Seq[Def.Setting[_]] = Seq()

  if (System.getProperty("include") != null ) {

    sTest = Seq(testOptions in Test += Tests.Argument("include", System.getProperty("include")))
  }

  if (System.getProperty("exclude") != null ) {
    sTest = Seq(testOptions in Test += Tests.Argument("exclude", System.getProperty("exclude")))
  }

  var jO: Seq[Def.Setting[_]] = Seq(javaOptions in Test += System.getProperty("waitSeconds"),
                                    testOptions in Test += Tests.Argument("sequential", "true"))

  var gS: Seq[Def.Setting[_]] = Seq(concurrentRestrictions in Global := Seq(Tags.limit(Tags.CPU, 4), Tags.limit(Tags.Network, 10), Tags.limit(Tags.Test, 4)))

  var f: Seq[Def.Setting[_]] = Seq(sbt.Keys.fork in Test := false)

  var jcoco: Seq[Def.Setting[_]] = Seq(parallelExecution in jacoco.Config := false)

  val keyStore = System.getProperty("sbt.carers.keystore")

  var keyStoreOptions: Seq[Def.Setting[_]] =  Seq(javaOptions in Test += ("-Dcarers.keystore=" + keyStore))

  var vS: Seq[Def.Setting[_]] = Seq(version := appVersion, libraryDependencies ++= appDependencies)

  var appSettings: Seq[Def.Setting[_]] =  sV ++ sO ++ sR ++ gS ++ sTest ++ jO ++ f ++ jcoco ++ keyStoreOptions ++ jacoco.settings ++ vS ++ net.virtualvoid.sbt.graph.Plugin.graphSettings

  val main = Project(appName, file(".")).enablePlugins(play.sbt.PlayScala,SbtWeb).settings(appSettings: _*)
}
