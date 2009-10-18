package shorty.db

import java.security._
import scala.actors._

sealed abstract class URIHashMessage;
case class URI(val uri:String) extends URIHashMessage;

class URIHasher(database:DB) extends Actor with Logs {

  var digestName:String = "sha-1"

  def size = database.size
  def close = database.close

  def act() {
    while(true) {
      receive {
        case uri:String => sender ! hash(uri)
        case x:Any => warn("No clue how to deal with a message " + x.toString)
      }
    }
  }

  private def hash(s:String):String = hash(s,0,6)
  private def hash(s:String,at:Int,til:Int) = {
    val digest = MessageDigest.getInstance(digestName)
    digest.update(s.getBytes())
    new String(digest.digest()) match {
      case x:String if x.length < til => x
      case x:String => x.substring(at,til)
    }
  }
}
