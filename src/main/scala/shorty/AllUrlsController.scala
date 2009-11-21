package shorty

import shorty.db._

/**
  * Controller for manipulating all the urls as a resource.
  * Basically just lets you add a new URL and get the hash back
  * @param hasher the hasher to use to hash urls
  * @param apiKey the key that must be in the request to allow hashing
  */
class AllUrlsController(hasher:URIHasher, apiKey:String) extends Controller {

  val URL_PARAM = "url"
  val API_PARAM = "api"

  override def post(params:Map[String,String]) = {
    debug("Post with url == " + params(URL_PARAM))
    if (apiKey.equals(params.getOrElse(API_PARAM,""))) {
      // this form returns an Option, and we are expecting an Option inside that
      hasher.!?(WAIT_TIME,HashURI(params(URL_PARAM))) match {
        case Some(Some(hash:String)) => Hash(hash)
        case Some(None) => Error(500,"Internal error from database")
        case None => timeout
      }
    }
    else {
      notAllowed
    }
  }
}
