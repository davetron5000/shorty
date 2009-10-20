package shorty.db

import java.io._

import com.sleepycat.je._

/** Berkeley DB, use the DB object to create instances. 
  * This is a low-level key/value interface.
  */
class DB(env:Environment,name:String) extends Logs {
  val database = {
    val config = new DatabaseConfig
    config.setAllowCreate(true)
    env.openDatabase(null,name,config)
  }

  private implicit def stringToDatabaseEntry(s:String) = new DatabaseEntry(s.getBytes())
  private implicit def databaseEntryToString(d:DatabaseEntry) = new String(d.getData)

  def size = database.count()

  def apply(key:String):Option[String] = {
    var value:DatabaseEntry = new DatabaseEntry
    database.get(null,key,value,null) match {
      case OperationStatus.SUCCESS => Some(value)
      case x:OperationStatus => {
        debug("Got " + x + " for " + key)
        None
      }
    }
  }

  def +=(entry:(String,String)) = {
    database.put(null,entry._1,entry._2)
  }

  def close = {
    database.close()
    env.close()
  }

  override def finalize = close
}

/** Factory for DB instances */
object DB extends Logs {
  /**
    * Create a new DB instance with the default name, using the given dir.
    * @param dir the directory where you want the db files created
    */
  def apply(dir:File):DB = apply(dir,"shorty")

  /**
    * Create a new DB instance, using the given dir.
    * @param dir the directory where you want the db files created
    * @param name the name of the db, if you care
    */
  def apply(dir:File, name:String) = {
    debug("Creating new DB " + name + " in " + dir)
    if (!dir.exists()) dir.mkdirs()

    val envConfig:EnvironmentConfig = new EnvironmentConfig();
    envConfig.setTransactional(false);
    envConfig.setAllowCreate(true);
    
    val environment = new Environment(dir,envConfig)
    new DB(environment,name)
  }
}
