package shorty

import org.easymock.EasyMock._
import org.easymock.EasyMock

import javax.servlet.http._

class TestMethodParser extends BaseTest {

  describe("MethodParser") {
    it ("should be properly parse") {
      testParsing("POST",POST)
      testParsing("GET",GET)
      testParsing("PUT",PUT)
      testParsing("DELETE",DELETE)
    }
    it ("should be case insensitive") {
      val request = createMock(classOf[HttpServletRequest])
      val parser = new AnyRef with MethodParser

      EasyMock.expect(request.getMethod()).andReturn("POST")
      EasyMock.expect(request.getHeader(parser.METHOD_HEADER)).andReturn(null)
      EasyMock.expect(request.getParameter(parser.METHOD_PARAM)).andReturn(null)
      replay(request)

      val method = parser.determineMethod(request)
      method should equal (POST)
    }

    it ("should be favor the header over the method") {
      val request = createMock(classOf[HttpServletRequest])
      val parser = new AnyRef with MethodParser

      EasyMock.expect(request.getMethod()).andReturn("POST")
      EasyMock.expect(request.getHeader(parser.METHOD_HEADER)).andReturn("delete")
      EasyMock.expect(request.getHeader(parser.METHOD_HEADER)).andReturn("delete")
      EasyMock.expect(request.getParameter(parser.METHOD_PARAM)).andReturn(null)
      replay(request)

      val method = parser.determineMethod(request)
      method should equal (DELETE)
    }

    it ("should be favor the param over the method") {
      val request = createMock(classOf[HttpServletRequest])
      val parser = new AnyRef with MethodParser

      EasyMock.expect(request.getMethod()).andReturn("POST")
      EasyMock.expect(request.getHeader(parser.METHOD_HEADER)).andReturn(null)
      EasyMock.expect(request.getParameter(parser.METHOD_PARAM)).andReturn("put")
      EasyMock.expect(request.getParameter(parser.METHOD_PARAM)).andReturn("put")
      replay(request)

      val method = parser.determineMethod(request)
      method should equal (PUT)
    }

    it ("should be favor the param over both") {
      val request = createMock(classOf[HttpServletRequest])
      val parser = new AnyRef with MethodParser

      EasyMock.expect(request.getMethod()).andReturn("POST")
      EasyMock.expect(request.getHeader(parser.METHOD_HEADER)).andReturn("delete")
      EasyMock.expect(request.getHeader(parser.METHOD_HEADER)).andReturn("delete")
      EasyMock.expect(request.getParameter(parser.METHOD_PARAM)).andReturn("put")
      EasyMock.expect(request.getParameter(parser.METHOD_PARAM)).andReturn("put")
      replay(request)

      val method = parser.determineMethod(request)
      method should equal (PUT)
    }
  }

  private def testParsing(methodString:String,methodObject:HTTPMethod) = {

    val request = createMock(classOf[HttpServletRequest])
    val parser = new AnyRef with MethodParser


    EasyMock.expect(request.getMethod()).andReturn(methodString)
    EasyMock.expect(request.getHeader(parser.METHOD_HEADER)).andReturn(null)
    EasyMock.expect(request.getParameter(parser.METHOD_PARAM)).andReturn(null)
    replay(request)

    val method = parser.determineMethod(request)
    method should equal (methodObject)
  }
}
