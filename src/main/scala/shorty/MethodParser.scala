package shorty

import javax.servlet.http._

trait MethodParser {

  val METHOD_PARAM = "_method";
  val METHOD_HEADER = "X-HTTP-Method-Override";

  /**
   * determines the request method.  The parameter takes most precedence,
   * followed by the header, followed by the HTTP request method.
   * THis allows for tunnelling
   */
  def determineMethod(request:HttpServletRequest) = {
    var method = request.getMethod
    if (request.getHeader(METHOD_HEADER) != null) {
      method = request.getHeader(METHOD_HEADER)
    }
    if (request.getParameter(METHOD_PARAM) != null) {
      method = request.getParameter(METHOD_PARAM)
    }
    method.toLowerCase
  }


}
