package shorty;

abstract class Controller extends Logs {
  val WAIT_TIME = 2000
  protected val notAllowed = new Left((405,"This method is not allowed"))
  protected val timeout = new Left((408,"Request timed out to database"))

  def get(params:Map[String,String]):Either[(Int,String),String] = notAllowed
  def put(params:Map[String,String]):Either[(Int,String),String] = notAllowed
  def post(params:Map[String,String]):Either[(Int,String),String] = notAllowed
  def delete(params:Map[String,String]):Either[(Int,String),String] = notAllowed
}
