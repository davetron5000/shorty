package shorty.db

/** Base of messages receivable by URIHasher */
sealed abstract class URIHashMessage;

/** Hashes the given URI and stores it.
  * The sender will receive a String message back with the
  * hash
  */
case class HashURI(val uri:String) extends URIHashMessage;

/** Retrieves the URI with the given hash as an Option[String] */
case class GetURI(val hash:String) extends URIHashMessage;

