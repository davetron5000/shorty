package shorty;

abstract class Controller extends Logs {
  private val notAllowed = new Left((405,"This method is not allowed"))

  def get:Either[(Int,String),String] = notAllowed
  def put:Either[(Int,String),String] = notAllowed
  def post:Either[(Int,String),String] = notAllowed
  def delete:Either[(Int,String),String] = notAllowed
}
