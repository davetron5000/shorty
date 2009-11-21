package shorty

import shorty.db._

/**
  * Routes requests based on path and method
  */
trait Router {

  /**
    * returns a Controller based upon the method and path
    */
  def route(path:List[String]):Option[Controller] = {
    path match {
      case Nil => Some(new AllUrlsController(uriHasher,apiKey))
      case x :: rest => Some(new OneUrlController(uriHasher,x))
    }
  }

  /** Provides access to a URI Hasher */
  protected def uriHasher:URIHasher;

  /** Provides the API key that we recognize */
  protected def apiKey:String;
}
