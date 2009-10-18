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
    database.close
  }

  protected def newDB:(File,DB) = {
    val tmpFile = File.createTempFile(getClass.getName,"db")
    tmpFile.delete
    tmpFile.mkdirs
    tmpFile.deleteOnExit
    (tmpFile,newDB(tmpFile))
  }

  protected def newDB(dir:File) = DB(dir)

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

    it ("should allow updating a value") {
      database.put("somekey","somevalue")
      database.put("somekey","some other value")
      database.get("somekey") should equal(Some("some other value"))
    }


    it ("should persist to disk") {
      var (tmpFile,diskDB) = newDB

      diskDB.put("somekey","somevalue")
      diskDB.close

      diskDB = newDB(tmpFile)
      diskDB.get("somekey") should equal(Some("somevalue"))
      diskDB.size should equal(1)
      diskDB.close
    }
  }
}
