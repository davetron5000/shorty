package shorty;

abstract class Controller extends Logs {
  private val notAllowed = new Left((405,"This method is not allowed"))

  def get(params:Map[String,String]):Either[(Int,String),String] = notAllowed
  def put(params:Map[String,String]):Either[(Int,String),String] = notAllowed
  def post(params:Map[String,String]):Either[(Int,String),String] = notAllowed
  def delete(params:Map[String,String]):Either[(Int,String),String] = notAllowed
}
