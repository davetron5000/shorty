package shorty.db

class DB {
  private var keyValue:Map[String,String] = Map()

  def size = 0
  def get(key:String):Option[String] = keyValue.get(key)
  def put(key:String,value:String) = keyValue = keyValue + ((key,value))
}
