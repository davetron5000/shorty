package shorty

import java.io._
import java.util._

import javax.servlet._
import javax.servlet.http._

/** Main entry point into the web framework */
class ShortyServlet extends HttpServlet with Logs with MethodParser {

  protected override def service(request:HttpServletRequest, response:HttpServletResponse) = {
    val method = determineMethod(request)
    //val path = getPath(request)

    response.getWriter.write("Hello")
  }

}

