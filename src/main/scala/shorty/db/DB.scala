package shorty.db

import java.io._

import com.sleepycat.je._

/** The lowest-level interface to the key/value store.
  * This is not thread-safe.
  */
trait DB {
  def size:Long = keyValue.size
  def get(key:String):Option[String] = keyValue.get(key)
  def put(key:String,value:String):Unit = {
    keyValue = keyValue + ((key,value))
  }
  def close = {}
  protected var keyValue:Map[String,String]
}

/** 
  * An implementation of DB using BerkelyDB
  */
class BDB(env:Environment) extends DB with Logs {
  protected var keyValue:Map[String,String] = Map()

  val database = {
    val config = new DatabaseConfig
    config.setAllowCreate(true)
    env.openDatabase(null,"shorty",config)
  }

  private implicit def stringToDatabaseEntry(s:String) = new DatabaseEntry(s.getBytes())

  override def size = database.count()
  override def get(key:String) = {
    var value:DatabaseEntry = new DatabaseEntry
    database.get(null,key,value,null) match {
      case OperationStatus.SUCCESS => Some(new String(value.getData))
      case x:OperationStatus => {
        debug("Got " + x + " for " + key)
        None
      }
    }
  }

  override def put(key:String, value:String) = {
    database.put(null,key,value)
  }

  override def close = {
    database.close()
    env.close()
    super.close
  }
}

object BDB {
  def apply(dir:File) = {
    if (!dir.exists()) dir.mkdirs()

    val envConfig:EnvironmentConfig = new EnvironmentConfig();
    envConfig.setTransactional(false);
    envConfig.setAllowCreate(true);
    
    val environment = new Environment(dir,envConfig)
    new BDB(environment)
  }
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
