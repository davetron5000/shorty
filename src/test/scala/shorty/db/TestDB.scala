package shorty.db

import java.io._

import shorty._

class TestDB extends BaseTest {

  var database:DB = _

  override def beforeEach = {
    val (file,db) = newDB
    database = db
  }

  override def afterEach = { 
  }

  private def newDB:(File,DB) = {
    val tmpFile = File.createTempFile(getClass.getName,"db")
    tmpFile.deleteOnExit
    (tmpFile,newDB(tmpFile))
  }

  private def newDB(file:File) = new DB(file)

  describe("DB") {
    it ("should initially be empty") {
      database.size should equal(0)
    }

    it ("should return None on get of unknown key") {
      database.get("nevertobeused") should equal(None)
    }

    it ("should return a get when it was put") {
      database.put("somekey","somevalue")
      database.get("somekey") should equal(Some("somevalue"))
      database.size should equal(1)
      database.get("nevertobeused") should equal(None)
    }
    it ("should persist to disk") {
      var (tmpFile,diskDB) = newDB

      diskDB.put("somekey","somevalue")

      diskDB = newDB(tmpFile)
      diskDB.get("somekey") should equal(Some("somevalue"))
      diskDB.size should equal(1)
    }
  }

}
