package shorty

import java.io._

import javax.servlet._
import javax.servlet.http._

import shorty.db._

/** Main entry point into the app */
class ShortyServlet extends HttpServlet 
  with Logs 
  with MethodParser 
  with Router 
  with RepresentationParser {

  override protected def uriHasher = {
    val hasher = getServletContext.getAttribute(ShortyServlet.URI_HASHER_ATTRIBUTE).asInstanceOf[URIHasher]
    if (hasher == null) throw new ServletException(ShortyServlet.URI_HASHER_ATTRIBUTE + " wasn't set in servlet context!")
      hasher
  }

  override protected def service(request:HttpServletRequest, response:HttpServletResponse) = {
      val path = getPath(request)
      val repType = determineRepresentation(request)
      route(path) match {
        case Some(controller) => {
          val result = determineMethod(request) match {
            case GET => controller.get(params(request))
            case PUT => controller.put(params(request))
            case POST => controller.post(params(request))
            case DELETE => controller.delete(params(request))
          }
          result match {
            case Left((httpError,message)) => response.sendError(httpError,message)
            case Right(url) => response.getWriter.println(url)
          }
        }
        case None => response.sendError(405)
    }
  }

  /** Parse the params from the request and return as a Scala map */
  private def params(request:HttpServletRequest) = {
    val e:java.util.Enumeration[_] = request.getParameterNames
    var map:Map[String,String] = Map.empty
    while (e.hasMoreElements) {
      val name = e.nextElement.asInstanceOf[String]
      map = map + ((name,request.getParameter(name)))
    }
    map
  }

  /** gets the path as a list of elements, stripping off the annoying blank you get sometimes */
  private def getPath(request:HttpServletRequest) = request.getPathInfo.split("/").toList match {
    case "" :: rest => rest
    case x => x
  }

}

object ShortyServlet {
  val DB_DIR_PARAM = "dbDir"
  val URI_HASHER_ATTRIBUTE = "uriHasher"
}
