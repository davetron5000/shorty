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
    def methodHelper = 
      if (request.getParameter(METHOD_PARAM) != null) 
        request.getParameter(METHOD_PARAM)
      else if (request.getHeader(METHOD_HEADER) != null) 
        request.getHeader(METHOD_HEADER)
      else 
        request.getMethod
    methodHelper.toLowerCase
  }


}
