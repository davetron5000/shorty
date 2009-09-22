package shorty

import java.io._
import java.util._

import javax.servlet._
import javax.servlet.http._

/** Main entry point into the web framework */
class ShortyServlet extends HttpServlet 
  with Logs 
  with MethodParser 
  with Router 
  with RepresentationParser {

  protected override def service(request:HttpServletRequest, response:HttpServletResponse) = {
    val method = determineMethod(request)
    val path = getPath(request)
    determineRepresentation(request) match {
      case Some(x) => {
        route(method,path) match {
          case Some(r) => response.getWriter.write(r + " as an " + x)
          case None => response.sendError(405)
        }
      }
      case None => response.sendError(406)
    }
  }

  private def getPath(request:HttpServletRequest) = request.getPathInfo.split("/").toList match {
    case "" :: rest => rest
    case x => x
  }

}

