package shorty.db

import java.security._
import java.math._
import scala.actors._

/** Base of messages receivable by URIHasher */
sealed abstract class URIHashMessage;

/** Hashes the given URI and stores it.
  * The sender will receive a String message back with the
  * hash
  */
case class HashURI(val uri:String) extends URIHashMessage;

/** Retrieves the URI with the given hash as an Option[String] */
case class GetURI(val hash:String) extends URIHashMessage;

/**
  * This hashes URIs to short character strings that we store
  * in a database.  This works as an Actor and receives 
  * messages that subclass URIHashMessage.
  *
  * <pre>
  * val hasher = URIHasher(db)
  * hasher.start
  * val hash = hasher !! HashURI("http://www.google.com")
  * println("The hashed value is " + hash())
  * </pre>
  *
  * @param database the DB instance to use to store the hashed urls
  * @param hasher a function that, given a URL, hashes it, and returns the hashed value. This function need
  * not trim or manipulate the string, just ensure it is in ASCII.
  *
  * @see shorty.db.URIHashMessage
  * @see shorty.db.GetURI
  * @see shorty.db.HashURI
  */
class URIHasher(database:DB, hasher: (String) => String) extends Actor with Logs {

  /** Get the number of URIs hashed */
  def size = database.size
  /** Close this hasher; this object is useless after this call and
    * all messages sent will get None in response 
    */
  def close = database.close

  /**
    * True if this URIHasher is no longer running
    */
  def closed = database.closed

  def act() {
    while(true) {
      receive {
        case HashURI(uri) if !closed => sender ! store(uri)
        case GetURI(hash) if !closed => sender ! load(hash)
        case x:URIHashMessage => {
          warn("Database has been closed")
          sender ! None
        }
        case Exit => {
          info("Exiting and closing URIHasher")
          close
          sender ! None
        }
        case x => {
          warn("No clue how to deal with a message " + x.toString)
          sender ! None
        }
      }
    }
  }

  private def store(uri:String) = Some((database += trim(hasher(uri)) -> uri)._1)
  private def load(h:String) = database(h)

  private val LENGTH = 6 
  private def trim(s:String) = s match {
    case x if x.length < LENGTH => x
    case x => x.substring(0,LENGTH)
  }
}

/**
  * Factory for creating URIHasher instances 
  */
object URIHasher {
  def apply(database:DB) = new URIHasher(database,new MessageDigestHasher("sha"))
  def apply(database:DB,hasher:(String) => String) = new URIHasher(database,hasher)
}

/**
  * A hash function that uses a message digest from the <tt>java.security</tt> package
  */
class MessageDigestHasher(digestName:String) extends Function1[String,String] with Logs {
  def apply(s:String) = {
    debug("Asked to hash " + s)
    val digest = MessageDigest.getInstance(digestName)
    digest.update(s.getBytes())
    new BigInteger(1,digest.digest()).toString(16)
  }
}
