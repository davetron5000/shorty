package shorty

import shorty.db._

/**
  * Controller for manipulating all the urls as a resource.
  * Basically just lets you add a new URL and get the hash back
  */
class AllUrlsController(hasher:URIHasher) extends Controller {

  val WAIT_TIME = 2000
  val URL_PARAM = "url"

  override def post(params:Map[String,String]) = {
    debug("Post with url == " + params(URL_PARAM))
    // this form returns an Option, and we are expecting an Option inside that
    hasher.!?(WAIT_TIME,HashURI(params(URL_PARAM))) match {
      case Some(Some(hash:String)) => new Right(hash)
      case Some(None) => new Left((500,"Internal error from database"))
      case None => new Left((500,"Timed out hashing the URL"))
    }
  }
}
