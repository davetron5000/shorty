package shorty

import org.apache.log4j._

/** Wraps log4j in a trait that not only makes it easy to log things
  * but also uses lazy evaluation for log messages -- no need to 
  * wrap them in an if statement for expensive messages
  *
  * <pre>
  * class Foo extends Logs {
  *   def doit = {
  *     debug("This is a " + very.complicated + " log message")
  *     fatal(someException)
  *     // etc.
  *   }
  * }
  * </pre>
  */
trait Logs {
  private[this] val logger = Logger.getLogger(getClass().getName());

  import org.apache.log4j.Level._

  def trace(message: => Unit) = if (logger.isEnabledFor(TRACE)) logger.trace(message)
  def trace(message: => Unit, ex:Throwable) = if (logger.isEnabledFor(TRACE)) logger.trace(message,ex)

  def debug(message: => Unit) = if (logger.isEnabledFor(DEBUG)) logger.debug(message)
  def debug(message: => Unit, ex:Throwable) = if (logger.isEnabledFor(DEBUG)) logger.debug(message,ex)

  def debugValue[T](valueName: String, value: => T):T = {
    val result:T = value
    debug(valueName + " == " + result.toString)
    result
  }

  def info(message: => Unit) = if (logger.isEnabledFor(INFO)) logger.info(message)
  def info(message: => Unit, ex:Throwable) = if (logger.isEnabledFor(INFO)) logger.info(message,ex)

  def warn(message: => Unit) = if (logger.isEnabledFor(WARN)) logger.warn(message)
  def warn(message: => Unit, ex:Throwable) = if (logger.isEnabledFor(WARN)) logger.warn(message,ex)

  def error(ex:Throwable) = if (logger.isEnabledFor(ERROR)) logger.error(ex.toString,ex)
  def error(message: => Unit) = if (logger.isEnabledFor(ERROR)) logger.error(message)
  def error(message: => Unit, ex:Throwable) = if (logger.isEnabledFor(ERROR)) logger.error(message,ex)

  def fatal(ex:Throwable) = if (logger.isEnabledFor(FATAL)) logger.fatal(ex.toString,ex)
  def fatal(message: => Unit) = if (logger.isEnabledFor(FATAL)) logger.fatal(message)
  def fatal(message: => Unit, ex:Throwable) = if (logger.isEnabledFor(FATAL)) logger.fatal(message,ex)
}
