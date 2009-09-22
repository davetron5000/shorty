package shorty

import java.util.Enumeration

import org.easymock.EasyMock._
import org.easymock.EasyMock

import javax.servlet.http._

class TestRepresentationParser extends BaseTest {
// I can't test this, since I cannot implement Enumeration

  /*
  describe("RepresentationParser") {
    it ("should be case insensitive") {
      val request = createMock(classOf[HttpServletRequest])
      val parser = new AnyRef with RepresentationParser

      val enumeration:Enumeration[T] forSome { type T } = createMock(classOf[Enumeration[T] forSome { type T }])
      EasyMock.expect(enumeration.hasMoreElements).andReturn(true)
      EasyMock.expect(enumeration.nextElement).andReturn("TEXT/HTML")
      EasyMock.expect(enumeration.hasMoreElements).andReturn(false)
      EasyMock.expect(request.getHeaders(parser.ACCEPT_HEADER)).andReturn(enumeration)
      replay(request)
      replay(enumeration)

      val t = parser.determineRepresentation(request)
      t should equal ("text/html")
    }

    it ("should default to HTML") {
      val request = createMock(classOf[HttpServletRequest])
      val parser = new AnyRef with RepresentationParser

      EasyMock.expect(request.getHeaders(parser.ACCEPT_HEADER)).andReturn(new EasyEnum(List()))
      replay(request)

      val t = parser.determineRepresentation(request)
      t should equal ("text/html")
    }

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
}
