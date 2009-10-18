package shorty.db

import java.io._

/** The lowest-level interface to the key/value store.
  * This is not thread-safe.
  */
trait DB {
  def size = keyValue.size
  def get(key:String):Option[String] = keyValue.get(key)
  def put(key:String,value:String):Unit = {
    keyValue = keyValue + ((key,value))
  }
  protected var keyValue:Map[String,String]
}

/** 
  * An implementation of DB using BerkelyDB
  */
abstract class BDB(dir:File) extends DB with Logs {
}

/**
  * A simple file-based key/value store
  */
class FileDB(file:File) extends DB with Logs {
  protected var keyValue:Map[String,String] = initFromFile

  override def put(key:String,value:String):Unit = {
    super.put(key,value)
    outputToFile
  }

  private def initFromFile = {
    if (file.exists()) {
      try {
        debug("Reading in " + file)
        val is = new ObjectInputStream(new FileInputStream(file))
        keyValue = is.readObject.asInstanceOf[Map[String,String]]
        is.close
      }
      catch {
        case eof:EOFException => {
          debug("EOFException While reading " + file)
          debug("Assumign empty file")
          keyValue = Map()
        }
        case ex:Exception => throw ex
      }
    }
    else {
      debug("File not there")
      keyValue = Map()
    }
    keyValue
  }

  private def outputToFile:Unit = {
    val os = new ObjectOutputStream(new FileOutputStream(file))
    os.writeObject(keyValue)
    os.close
  }
}
