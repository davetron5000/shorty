package shorty

import java.util.Enumeration

import javax.servlet.http._

/**
  * Parses the representation from the request.
  * This could be either an explicit parameter or the
  * Accept: header
  */
trait RepresentationParser extends Logs {
  val ACCEPT_HEADER = "Accept"
  val TYPE_PARAM = "_type"
  val DEFAULT_TYPE = "text/html"

  val knownTypes = List( DEFAULT_TYPE, "text/xml","application/json")

  /**
    * given a request, returns the representation (mime type) to be
    * used
    * @return the name of the mime type, never null
    */
  def determineRepresentation(request:HttpServletRequest) = {
    fromParam(request) match {
      case Some(x) => x
      case None => fromHeader(request) match {
        case Some(y) => y
        case None => DEFAULT_TYPE
      }
    }
  }

  private def fromHeader(request:HttpServletRequest) = {
    val requestedTypes = requested(request.getHeaders(ACCEPT_HEADER),Nil)
      knownTypes.intersect(requestedTypes) match {
        case x :: rest => {
          debug("Found mime type " + x)
          Some(x.toLowerCase)
        }
        case _ => {
          debug("No intersection between " + knownTypes.toString + " and " + requestedTypes.toString)
          None
        }
    }
  }

  private def fromParam(request:HttpServletRequest):Option[String] = {
    val param = request.getParameter(TYPE_PARAM)
    if (param != null) {
      if (knownTypes.contains(param.trim.toLowerCase) ) {
        return Some(request.getParameter(TYPE_PARAM).trim.toLowerCase)
      }
    }
    None
  }

  private def requested(enumeration:Enumeration[_],list:List[String]):List[String] = {
    if (!enumeration.hasMoreElements) {
      debug("None left, returning " + list.toString)
      list
    }
    else {
      val values = enumeration.nextElement.asInstanceOf[String]
      debug("Got more; so far " + list.toString)
      requested(enumeration,values.split(",").toList.map( _.toLowerCase ) ::: list)
    }
  }
}
