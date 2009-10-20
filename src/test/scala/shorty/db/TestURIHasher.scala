package shorty.db

import java.io._

import scala.actors._

import org.scalatest._
import org.scalatest.matchers._


import shorty._

class TestURIHasher extends BaseTest {

  var hasher:URIHasher = _

  override def beforeEach = {
    val (file,db) = newDB
    hasher = URIHasher(db)
  }

  override def afterEach = { 
    hasher.close
  }

  protected def newDB:(File,DB) = {
    val tmpFile = File.createTempFile(getClass.getName,"db")
    tmpFile.delete
    tmpFile.mkdirs
    tmpFile.deleteOnExit
    (tmpFile,newDB(tmpFile))
  }

  protected def newDB(dir:File) = DB(dir)

  describe("Hasher") {
    it ("should initially be empty") {
      hasher.size should equal(0)
    }
    it ("should return the hash for a URI") {
      hasher.start
      val future = hasher !! HashURI("http://www.google.com")
      future() should equal (Some("738ddf"))
    }
    it ("should not return a URI for an unknown hash") {
      hasher.start
      var future = hasher !!  GetURI("4e13de")
      future() should equal(None)

      future = hasher !! HashURI("http://www.google.com")
      future() should equal(Some("738ddf"))

      future = hasher !! GetURI("738ddf")
      future() should equal (Some("http://www.google.com"))

      future = hasher !!  GetURI("blah")
      future() should equal(None)
    }
    it ("should respond with None if DB is closed") {
      val (file,db) = newDB
      val myHasher = URIHasher(db)
      myHasher.start
      db.close
      val future = myHasher !! HashURI("http://www.yahoo.com")
      future() should equal(None)
    }
  }
}

