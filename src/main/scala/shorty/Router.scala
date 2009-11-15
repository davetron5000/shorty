package shorty

/**
  * Routes requests based on path and method
  */
trait Router {
  /**
    * returns a Controller based upon the method and path
    */
  def route(path:List[String]):Option[Controller] = {
    path match {
      case Nil => Some(new AllUrlsController)
      case x :: rest => Some(new OneUrlController(x))
    }
  }
}
