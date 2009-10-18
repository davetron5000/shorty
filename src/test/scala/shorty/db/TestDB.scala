package shorty.db

import java.io._

import shorty._

class TestDB extends BaseDBTest {

  protected def newDB:(File,DB) = {
    val tmpFile = File.createTempFile(getClass.getName,"db")
    tmpFile.deleteOnExit
    (tmpFile,newDB(tmpFile))
  }

  protected def newDB(file:File) = new FileDB(file)
  describe("FileDB") {
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
  }
}
