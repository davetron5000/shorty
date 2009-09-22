package shorty

trait Router {
  def route(method:String, path:List[String]) = {
    (method,path) match {
      case ("get",Nil) => Some("get all")
      case ("get",x :: rest) => Some("get " + x)
      case ("post",Nil) => Some("post new")
      case ("put",x :: rest) => Some("create new named " + x)
      case ("delete",x :: rest) => Some("delete existing named " + x)
      case _ => None
    }
  }
}
