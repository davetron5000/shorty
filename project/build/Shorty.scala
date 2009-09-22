import sbt._

class Shorty(info: ProjectInfo) extends DefaultWebProject(info) {

  val servlet = "javax.servlet" % "servlet-api" % "2.5" % "provided->default"
  val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.14" % "test->default"
  val scalatest = "org.scala-tools.testing" % "scalatest" % "0.9.5" % "test->default"
  val jwebunit = "net.sourceforge.jwebunit" % "jwebunit-htmlunit-plugin" % "1.5" % "test->default"
  val log4j = "log4j" % "log4j" % "1.2.13" % "provided->default" 
  val easymockclass = "easymock" % "easymockclassextension" % "2.2" % "test->default"

  override def jettyContextPath = "/s"

  override def defaultTestTask(testOptions: => Seq[TestOption]) =
    jettyStop dependsOn {
      super.defaultTestTask(testOptions) dependsOn {
        jettyRestart
      }
    }

}
