package rebootjs

import sbt._
import Keys._
import scala.scalajs.sbtplugin.ScalaJSPlugin._
import ScalaJSKeys._

object RebootJsBuild extends Build {

  lazy val rebootjs = (Project("reboot-js", file("."))
    settings(
      name := "reboot-js",
      organization := "org.gnieh",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.2",
      scalacOptions ++= Seq("-feature", "-deprecation")
    )
    settings(scalaJSSettings: _*)
    settings(
      unmanagedSources in (Compile, packageJS) += baseDirectory.value / "src" / "main" / "js" / "startup.js"
      //sourceDirectories in Compile += baseDirectory.value  / "src" / "example" / "scala"
    )
  ) dependsOn(scalajsdom)

  lazy val scalajsdom = uri("git://github.com/scala-js/scala-js-dom.git")

}
