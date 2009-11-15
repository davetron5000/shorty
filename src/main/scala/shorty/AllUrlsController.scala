package shorty

import shorty.db._

class AllUrlsController(hasher:URIHasher) extends Controller {
  val WAIT_TIME = 2000
  override def post(params:Map[String,String]) = {
    debug("Post with url == " + params("url"))
    hasher.!?(WAIT_TIME,HashURI(params("url"))) match {
      case Some(Some(hash:String)) => new Right(hash)
      case Some(None) => new Left((500,"Internal error from database"))
      case None => new Left((500,"Timed out hashing the URL"))
    }
  }
}
