package shorty

import java.util.Enumeration

import org.easymock.EasyMock._
import org.easymock.EasyMock

import javax.servlet.http._

class TestRepresentationParser extends BaseTest {

  describe("RepresentationParser") {
    it ("should be case insensitive") {
      val request = createMock(classOf[HttpServletRequest])
      val parser = new AnyRef with RepresentationParser

      val enumeration = expectOnEnum(parser,request,"TEXT/HTML")

      EasyMock.expect(request.getParameter(parser.TYPE_PARAM)).andReturn(null)
      replay(request)
      replay(enumeration)

      val t = parser.determineRepresentation(request)
      t should equal (Some("text/html"))
    }

    it ("should default to HTML") {
      val request = createMock(classOf[HttpServletRequest])
      val parser = new AnyRef with RepresentationParser

      val enumeration = expectOnEnum(parser,request,List())

      EasyMock.expect(request.getParameter(parser.TYPE_PARAM)).andReturn(null)  

      replay(request)

      val t = parser.determineRepresentation(request)
      t should equal (Some("text/html"))
    }
    /*

    it ("should be favor accept header") {
      val request = createMock(classOf[HttpServletRequest])
      val parser = new AnyRef with RepresentationParser

      EasyMock.expect(request.getHeaders(parser.ACCEPT_HEADER)).andReturn(new EasyEnum(List("TEXT/xml")))
      replay(request)

      val t = parser.determineRepresentation(request)
      t should equal ("text/xml")
    }
    */
  }

  private def expectOnEnum(parser: RepresentationParser, request:HttpServletRequest, mimeType:String):Enumeration[String] = expectOnEnum(parser,request,List(mimeType))
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

