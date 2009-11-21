package shorty;

sealed abstract class ControllerResponse;

case class Error(httpStatus:Int, message:String) extends ControllerResponse
case class URL(url:String) extends ControllerResponse
case class Hash(hash:String) extends ControllerResponse
