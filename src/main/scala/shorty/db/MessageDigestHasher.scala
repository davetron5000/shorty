package shorty.db

import java.security._
import java.math._

import shorty._

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
