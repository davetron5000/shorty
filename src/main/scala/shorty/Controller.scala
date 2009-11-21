package shorty;

abstract class Controller extends Logs {
  val WAIT_TIME = 2000
  protected val notAllowed = new Error(405,"This method is not allowed")
  protected val timeout = new Error(408,"Request timed out to database")

  def get(params:Map[String,String]):ControllerResponse = notAllowed
  def put(params:Map[String,String]):ControllerResponse = notAllowed
  def post(params:Map[String,String]):ControllerResponse = notAllowed
  def delete(params:Map[String,String]):ControllerResponse = notAllowed
}
