package shorty

import java.util.Enumeration

import org.easymock.EasyMock._
import org.easymock.EasyMock

import javax.servlet.http._

class TestRepresentationParser extends BaseTest {

  var request:HttpServletRequest = _
  var parser:RepresentationParser = _

  override def beforeEach() = {
      request = createMock(classOf[HttpServletRequest])
      parser = new AnyRef with RepresentationParser
  }

  describe("RepresentationParser") {
    it ("should be case insensitive") {
      runTest(List("TEXT/XML"),null,"text/xml")
    }

    it ("should default to HTML") {
      runTest(List(),null,"text/html")
    }

    it ("should favor HTML when it gets more than one") {
      // not totally sure this actually works
      runTest(List("text/xml", "text/xml", "text/xml","text/html","image/png"),null,"text/html")
    }

    it ("should be favor accept header") {
      runTest(List("text/xml"),"application/json","text/xml")
    }
  }

  private def runTest(headers:List[String],typeParam:String,expected:String) = {
      val enumeration = expectOnEnum(parser,request,headers)

      EasyMock.expect(request.getParameter(parser.TYPE_PARAM)).andReturn(typeParam).anyTimes()
      replay(request)
      replay(enumeration)

      val t = parser.determineRepresentation(request)
      t should equal (expected)
  }

  private def expectOnEnum(parser: RepresentationParser, request:HttpServletRequest, mimeTypes:List[String]):Enumeration[String] = {
      val enumeration = createMock(classOf[java.util.Enumeration[String]])

      mimeTypes.foreach ( (mimeType) => {
        EasyMock.expect(enumeration.hasMoreElements).andReturn(true)
        EasyMock.expect(enumeration.nextElement).andReturn(mimeType)
      })

      EasyMock.expect(enumeration.hasMoreElements).andReturn(false)
      EasyMock.expect(request.getHeaders(parser.ACCEPT_HEADER))
      expectLastCall.andReturn(enumeration)
      enumeration
  }
}

