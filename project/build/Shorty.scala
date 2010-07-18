import sbt._

class Shorty(info: ProjectInfo) extends DefaultWebProject(info) {

  val oracleRepository = "Oracle Maven Repository" at "http://download.oracle.com/maven"
  val scalaSnapshots = "scala-tools.org SNAPSHOT Repository" at "http://scala-tools.org/repo-snapshots/"

  val servlet = "javax.servlet" % "servlet-api" % "2.5" % "provided->default"
  val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.14" % "test->default"
  val scalatest = "org.scalatest" % "scalatest" % "1.2" % "test->default"
  val jwebunit = "net.sourceforge.jwebunit" % "jwebunit-htmlunit-plugin" % "1.5" % "test->default"
  val log4j = "log4j" % "log4j" % "1.2.13" % "provided->default" 
  val easymockclass = "easymock" % "easymockclassextension" % "2.2" % "test->default"
  val berkeleydb = "com.sleepycat" % "je" % "4.0.71"

  override def jettyContextPath = "/s"

  /*
  override def defaultTestTask(testOptions: => Seq[TestOption]) =
    jettyStop dependsOn {
      super.defaultTestTask(testOptions) dependsOn {
        jettyRestart
      }
    }
    */
}
