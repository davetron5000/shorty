package shorty.db

import scala.actors._

sealed abstract class URIHashMessage;
case class URI(val uri:String) extends URIHashMessage;

class URIHasher(database:DB) extends Actor with Logs {
  def size = database.size
  def close = database.close

  def act() {
    while(true) {
      receive {
        case _ => sender ! "1234"
      }
    }
  }
}
