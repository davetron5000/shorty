package shorty.db

import java.io._

import shorty._

class TestBDB extends BaseDBTest {

  protected def newDB:(File,DB) = {
    val tmpFile = File.createTempFile(getClass.getName,"db")
    tmpFile.delete
    tmpFile.mkdirs
    tmpFile.deleteOnExit
    (tmpFile,newDB(tmpFile))
  }

  protected def newDB(dir:File) = BDB(dir)
}
