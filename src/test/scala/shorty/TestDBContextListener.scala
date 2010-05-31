package shorty

import java.io._
import javax.servlet._

import org.easymock.classextension.EasyMock._
import org.easymock.classextension.EasyMock
import org.easymock.{EasyMock => InterfaceEasyMock}
import shorty.db._

class TestDBContextListener extends BaseTest with Logs {

  def createMocks(dir:String) = {
    val event = createMock(classOf[ServletContextEvent])
    val context = createMock(classOf[ServletContext])

    InterfaceEasyMock.expect(context.log(InterfaceEasyMock.isA(classOf[String]))).anyTimes
    InterfaceEasyMock.expect(event.getServletContext).andReturn(context).anyTimes
    InterfaceEasyMock.expect(context.getInitParameter(ShortyServlet.DB_DIR_PARAM)).andReturn(dir)
    InterfaceEasyMock.expect(context.setAttribute(
      InterfaceEasyMock.eq(ShortyServlet.URI_HASHER_ATTRIBUTE),
      InterfaceEasyMock.isA(classOf[URIHasher]))).anyTimes

    replay(event)
    replay(context)
    (event,context)
  }

  describe("DBContextListener") {
    it ("should create a uri hasher") {
      val tmpDir = System.getProperty("java.io.tmpdir")
      val (event,context) = createMocks(tmpDir)

      val listener = new DBContextListener
      listener.contextInitialized(event)
      listener.contextDestroyed(event)

      verify(event)
      verify(context)
    }
    it ("should complain about non-existent directory") {
      val(event,context) = createMocks("/blah")

      val listener = new DBContextListener
      val exception = intercept[ServletException] {
        listener.contextInitialized(event)
      }
      exception.getMessage should include ("doesn't exist")

      verify(event)
      verify(context)
    }

    it ("should complain existent directory that is actually a file") {
      val tmpFile = File.createTempFile("shorty","test")
      val(event,context) = createMocks(tmpFile.getAbsolutePath)

      val listener = new DBContextListener
      val exception = intercept[ServletException] {
        listener.contextInitialized(event)
      }
      exception.getMessage should include ("is not a directory")

      verify(event)
      verify(context)
    }

    it ("should complain that no dir was configured") {
      val(event,context) = createMocks(null)

      val listener = new DBContextListener
      val exception = intercept[ServletException] {
        listener.contextInitialized(event)
      }
      exception.getMessage should include ("You must supply a value")

      verify(event)
      verify(context)
    }
  }
}
