package shorty

import java.util.Enumeration

import javax.servlet.http._

trait RepresentationParser extends Logs {
  val ACCEPT_HEADER = "Accept"
  val TYPE_PARAM = "_type"

  val knownTypes = List( "text/html","text/xml","application/json")

  def determineRepresentation(request:HttpServletRequest) = {
    val param = request.getParameter(TYPE_PARAM)
    if (param != null) {
      if (knownTypes.contains(param) ) {
      Some(request.getParameter(TYPE_PARAM).toLowerCase)
      }
      else {
        None
      }
    }
    else {
      val requestedTypes = requested(request.getHeaders(ACCEPT_HEADER),Nil)
      val inter = for( t <- knownTypes; r <- requestedTypes if (t.equalsIgnoreCase(r))) yield t
      inter match {
        case x :: rest => Some(x.toLowerCase)
        case _ => None
      }
    }
  }

  def requested(enumeration:Enumeration[_],l:List[String]):List[String] = {
    if (enumeration.hasMoreElements) {
      val values = enumeration.nextElement.asInstanceOf[String]
      debug("Got more; so far " + l.toString)
      requested(enumeration,values.split(",").toList ::: l)
    }
    else {
      debug("None left, returning " + l.toString)
      l
    }
  }
}
