package shorty

import java.io._
import javax.servlet._

import shorty.db._

class DBContextListener extends ServletContextListener {

  private var uriHasher:URIHasher = _

  override def contextInitialized(event:ServletContextEvent) = {
    event.getServletContext.log("Initializing our hasher/DB")
    getDBDir(event) match {
      case Right(dir) => {
        val hasher = URIHasher(DB(dir))
        hasher.start
        uriHasher = hasher
        event.getServletContext.setAttribute(ShortyServlet.URI_HASHER_ATTRIBUTE,uriHasher)
      }
      case Left(errorMessage) => throw new ServletException(errorMessage)
    }
  }

  private[this] def getDBDir(event:ServletContextEvent):Either[String,File] = {
    val dirName = event.getServletContext.getInitParameter(ShortyServlet.DB_DIR_PARAM)
    if (dirName != null) {
      val dir = new File(dirName)
      if (dir.exists) {
        if (dir.isDirectory) {
          Right(dir)
        }
        else {
          Left(dir.getAbsolutePath + " is not a directory")
        }
      }
      else {
        Left(dir.getAbsolutePath + " doesn't exist")
      }
    }
    else {
      Left("You must supply a value for " + ShortyServlet.DB_DIR_PARAM)
    }
  }

  override def contextDestroyed(event:ServletContextEvent) = {
    event.getServletContext.log("Shutting down our hasher/DB")
    if (uriHasher != null)
      uriHasher.close
  }
}
