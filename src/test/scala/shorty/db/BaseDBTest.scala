package shorty.db

import java.io._

import shorty._

/**
  * Base test to test any implementation of DB
  */
abstract class BaseDBTest extends BaseTest {

  var database:DB = _

  override def beforeEach = {
    val (file,db) = newDB
    database = db
  }

  override def afterEach = { 
  }

  protected def newDB:(File,DB)
  protected def newDB(file:File):DB

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

    it ("should deal with a non-existent file") {
      var (tmpFile,diskDB) = newDB
      diskDB.put("key","value")
      tmpFile.delete
      diskDB = newDB(tmpFile)
      diskDB.size should equal(0)

      tmpFile.delete
      diskDB.put("key","value")
      diskDB.size should equal(1)
      diskDB = newDB(tmpFile)
      diskDB.size should equal(1)
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
