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

  override protected def apiKey = initParam(ShortyServlet.API_KEY_PARAM)

  protected def hostname = initParam(ShortyServlet.HOST_NAME_PARAM)

  private def initParam(s:String) = {
    val param = getInitParameter(s)
    if (param == null) {
      throw new ServletException("You must set " + s + " as an init parameter")
    }
    param
  }

  override protected def service(request:HttpServletRequest, response:HttpServletResponse) = {
      response.setCharacterEncoding("utf-8")
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
          log("Got result " + result.toString)
          result match {
            case URL(url) => repType match {
              case "text/html" => response.sendRedirect(url)
              case "text/xml" => {
                response.setContentType(repType)
                response.getWriter.println(<longUrl>{url}</longUrl>.toString)
              }
              case "application/json" => {
                response.setContentType(repType)
                response.getWriter().println("{ \"longUrl\": \"" + url + "\" }")
              }
              case _ => response.getWriter.println(url)
            }
            case Hash(hash) => {
              val shortUrl = hostname + request.getContextPath + "/" + hash
              response.setContentType(repType)
              repType match {
                case "text/xml" => response.getWriter.println(<shortUrl>{shortUrl}</shortUrl>.toString)
                case "application/json" => response.getWriter().println("{ \"shortUrl\": \"" + shortUrl + "\" }")
                case "text/html" => response.getWriter.println(<a href={shortUrl}>{shortUrl}</a>.toString)
                case _ => response.getWriter.println(shortUrl)
              }
            }
            case Error(httpError,message) => response.sendError(httpError,message)
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
  val API_KEY_PARAM = "apiKey"
  val HOST_NAME_PARAM = "hostname"
}
