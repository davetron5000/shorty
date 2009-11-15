package shorty

import java.io._
import java.util._

import javax.servlet._
import javax.servlet.http._

import shorty.db._

/** Main entry point into the app */
class ShortyServlet extends HttpServlet 
  with Logs 
  with MethodParser 
  with Router 
  with RepresentationParser {

  val DB_DIR_PARAM = "dbDir"

  private var _uriHasher:URIHasher = _
  override protected def uriHasher = _uriHasher

  override protected def service(request:HttpServletRequest, response:HttpServletResponse) = {
    val method = determineMethod(request)
      val path = getPath(request)
      val repType = determineRepresentation(request)
      route(path) match {
        case Some(controller) => response.getWriter.write(controller.getClass.toString)
        case None => response.sendError(405)
    }
  }

  override def init() = {
    val dirName = getServletConfig.getInitParameter(DB_DIR_PARAM)
    if (dirName != null) {
      val dir = new File(dirName)
      if (dir.exists()) {
        if (dir.isDirectory()) {
          val hasher = URIHasher(DB(dir))
          hasher.start
          _uriHasher = hasher
        }
        else {
          throw new ServletException(dirName + " is not a directory")
        }
      }
      else {
        throw new ServletException(dirName + " doesn't exist")
      }
    }
    else {
      throw new ServletException("You must supply a value for " + DB_DIR_PARAM)
    }
  }

  /** gets the path as a list of elements, stripping off the annoying blank you get sometimes */
  private def getPath(request:HttpServletRequest) = request.getPathInfo.split("/").toList match {
    case "" :: rest => rest
    case x => x
  }

}

