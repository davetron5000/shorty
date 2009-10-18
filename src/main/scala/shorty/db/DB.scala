package shorty.db

import java.io._

class DB(file:File) extends Logs {
  private var keyValue:Map[String,String] = initFromFile

  def size = keyValue.size
  def get(key:String):Option[String] = keyValue.get(key)
  def put(key:String,value:String) = {
    keyValue = keyValue + ((key,value))
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

  private def outputToFile = {
    val os = new ObjectOutputStream(new FileOutputStream(file))
    os.writeObject(keyValue)
    os.close
  }
}
