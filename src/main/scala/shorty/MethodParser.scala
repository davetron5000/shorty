package shorty

import javax.servlet.http._

abstract sealed class HTTPMethod

case object GET extends HTTPMethod
case object PUT extends HTTPMethod
case object POST extends HTTPMethod
case object DELETE extends HTTPMethod

trait MethodParser {

  val METHOD_PARAM = "_method";
  val METHOD_HEADER = "X-HTTP-Method-Override";

  /**
   * Determines the request method based up on the requests headers, parameters and the HTTP method.  
   * The parameter takes most precedence,
   * followed by the header, followed by the HTTP request method.
   * This allows for tunnelling.
   * @param request the servlet request
   * @return string representation of the HTTP request method, all lower case, never null
   */
  def determineMethod(request:HttpServletRequest) = {
    def methodHelper = 
      if (request.getParameter(METHOD_PARAM) != null) 
        request.getParameter(METHOD_PARAM)
      else if (request.getHeader(METHOD_HEADER) != null) 
        request.getHeader(METHOD_HEADER)
      else 
        request.getMethod
    methodHelper.toLowerCase match {
      case "get" => GET
      case "put" => PUT
      case "post" => POST
      case "delete" => DELETE
      case x => throw new IllegalArgumentException("Method " + x.toString + " is not known")
    }
  }


}
