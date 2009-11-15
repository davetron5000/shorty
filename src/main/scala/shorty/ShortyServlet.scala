package shorty

import java.io._
import java.util._

import javax.servlet._
import javax.servlet.http._

/** Main entry point into the app */
class ShortyServlet extends HttpServlet 
  with Logs 
  with MethodParser 
  with Router 
  with RepresentationParser {

  protected override def service(request:HttpServletRequest, response:HttpServletResponse) = {
    val method = determineMethod(request)
      val path = getPath(request)
      val repType = determineRepresentation(request)
      route(path) match {
        case Some(controller) => response.getWriter.write(controller.getClass.toString)
        case None => response.sendError(405)
    }
  }

  /** gets the path as a list of elements, stripping off the annoying blank you get sometimes */
  private def getPath(request:HttpServletRequest) = request.getPathInfo.split("/").toList match {
    case "" :: rest => rest
    case x => x
  }

}

