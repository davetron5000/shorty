package shorty.db

import java.io._

import shorty._

class TestURIHasher extends BaseTest {

  var hasher:URIHasher = _

  override def beforeEach = {
    val (file,db) = newDB
    hasher = new URIHasher(db)
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
  }
}
