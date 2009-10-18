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
}
