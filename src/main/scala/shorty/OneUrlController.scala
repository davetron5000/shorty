package shorty;

import shorty.db._

/**
  * Controller providing access to one hashed URI, namely to 
  * look up the shortened URI to get the longer one
  */
class OneUrlController(hasher:URIHasher, val hash:String) extends Controller {

  override def get(params:Map[String,String]) = {
    hasher.!?(WAIT_TIME,GetURI(hash)) match {
      case Some(Some(url:String)) => new Right(url)
      case Some(None) => new Left((404,"No url is hashed under " + hash))
      case None => timeout
    }
  }

}
