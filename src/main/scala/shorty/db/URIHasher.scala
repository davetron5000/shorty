package shorty.db

import java.security._
import java.math._
import scala.actors._

sealed abstract class URIHashMessage;

/** Hashes the given URI and stores it.
  * The sender will receive a String message back with the
  * hash
  */
case class HashURI(val uri:String) extends URIHashMessage;

/** Retrieves the URI with the given hash as an Option[String] */
case class GetURI(val hash:String) extends URIHashMessage;

/**
  * This hashes URIs to 6-character strings that we store
  * in a database.  This works as an Actor and receives 
  * messages that subclass URIHashMessage.
  */
class URIHasher(database:DB, hasher: (String) => (String,String)) extends Actor with Logs {

  def size = database.size
  def close = database.close

  def act() {
    while(true) {
      receive {
        case HashURI(uri) => sender ! store(uri)
        case GetURI(hash) => sender ! load(hash)
        case x:Any => {
          warn("No clue how to deal with a message " + x.toString)
          sender ! None
        }
      }
    }
  }

  private def store(uri:String) = Some((database += hasher(uri))._1)
  private def load(h:String) = database(h)
}

object URIHasher {
  def apply(database:DB) = new URIHasher(database,new MessageDigestHasher("sha"))
  def apply(database:DB,hasher:(String) => (String,String)) = new URIHasher(database,hasher)
}

abstract class Hasher extends Function1[String,(String,String)]

class MessageDigestHasher(digestName:String) extends Hasher with Logs {
  def apply(s:String) = {
    debug("Asked to hash " + s)
    val digest = MessageDigest.getInstance(digestName)
    digest.update(s.getBytes())
    val result = new BigInteger(1,digest.digest()).toString(16) match {
      case x:String if x.length < 6 => x
      case x:String => x.substring(0,6)
    }
    debug("Hash is " + result)
    (result,s)
  }
}
