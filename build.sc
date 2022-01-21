import $ivy.`com.lihaoyi::mill-contrib-scoverage:$MILL_VERSION`
import mill._
import mill.contrib.scoverage.ScoverageModule
import mill.define.Target
import mill.modules.Jvm.JarManifest
import mill.scalalib._
import mill.scalalib.publish._

import java.util.jar.Attributes.Name.{IMPLEMENTATION_VERSION => ImplementationVersion}

private val packageVersion = "0.0.1"

/** A base trait for all test targets. */
trait ScalaTest extends TestModule {
  override def ivyDeps = Agg(ivy"org.scalatest::scalatest::3.1.3".excludeOrg(organizations = "org.junit"))
  override def testFramework: Target[String] = T { "org.scalatest.tools.Framework" }
}

/** The bimap Scala package package. */
object bimap extends ScalaModule with PublishModule with ScoverageModule {
  object test extends Tests with ScalaTest with ScoverageTests

  def scalaVersion     = "2.13.7"
  def scoverageVersion = "1.4.1"
  def publishVersion   = T { packageVersion }

  /** POM publishing settings for this package. */
  def pomSettings: Target[PomSettings] = PomSettings(
    description    = "A modern bidirectional map in Scala",
    organization   = "io.cvbio",
    url            = "https://github.com/clintval/bimap",
    licenses       = Seq(License.MIT),
    versionControl = VersionControl.github(owner = "clintval", repo = "bimap", tag = Some(packageVersion)),
    developers     = Seq(Developer(id = "clintval", name = "Clint Valentine", url = "https://github.com/clintval"))
  )

  /** The artifact name, fully resolved within the coordinate. */
  override def artifactName: T[String] = T { "io.cvbio.collection.mutable.bimap" }

  /** The JAR manifest. */
  override def manifest: T[JarManifest] = super.manifest().add(ImplementationVersion.toString -> packageVersion)

  /** All Scala compiler options for this package. */
  override def scalacOptions: T[Seq[String]] = T {
    Seq(
      "-opt:inline", // Turn on the inliner.
      "-opt-inline-from:io.cvbio.**", // Tells the inliner that it is allowed to inline things from these classes.
      "-Yopt-log-inline", "_", // Optional, logs the inliner activity so you know it is doing something.
      "-Yopt-inline-heuristics:at-inline-annotated", // Tells the inliner to use your `@inliner` tags.
      "-opt-warnings:at-inline-failed", // Tells you if methods marked with `@inline` cannot be inlined, so you can remove the tag.
      // The following are sourced from https://nathankleyn.com/2019/05/13/recommended-scalac-flags-for-2-13/
      "-deprecation", // Emit warning and location for usages of deprecated APIs.
      "-explaintypes", // Explain type errors in more detail.
      "-feature", // Emit warning and location for usages of features that should be imported explicitly.
      "-unchecked", // Enable additional warnings where generated code depends on assumptions.
      "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
      "-Xfatal-warnings", // Fail the compilation if there are any warnings.
      "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
      "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
      "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
      "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
      "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
      "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
      "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
      "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
      "-Xlint:option-implicit", // Option.apply used implicit view.
      "-Xlint:package-object-classes", // Class or object defined in package object.
      "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
      "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
      "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
      "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
      "-Ywarn-dead-code", // Warn when dead code is identified.
      "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
      "-Ywarn-numeric-widen", // Warn when numerics are widened.
      "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
      "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
      "-Ywarn-unused:locals", // Warn if a local definition is unused.
      "-Ywarn-unused:params", // Warn if a value parameter is unused.
      "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.
      "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
      "-Ywarn-unused:privates", // Warn if a private member is unused.
      "-Ybackend-parallelism", Math.min(Runtime.getRuntime.availableProcessors(), 8).toString, // Enable parallelization — scalac max is 16.
      "-Ycache-plugin-class-loader:last-modified", // Enables caching of classloaders for compiler plugins
      "-Ycache-macro-class-loader:last-modified", // and macro definitions. This can lead to performance improvements.
    )
  }
}
