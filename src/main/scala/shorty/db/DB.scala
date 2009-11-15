package shorty.db

import java.io._

import com.sleepycat.je._

/** Access to a Berkeley DB. 
  * This is a low-level key/value interface.
  * Use the DB object to create instances. 
  * @param env The Berkeley DB Environment object to use to create a database.
  * @param name the name of the database.  If this exists in <tt>env</tt>, it will be loaded.
  * Otherwise, it will be created.
  */
class DB(env:Environment,name:String) extends Logs {
  val database = {
    val config = new DatabaseConfig
    config.setAllowCreate(true)
    env.openDatabase(null,name,config)
  }

  private implicit def stringToDatabaseEntry(s:String) = new DatabaseEntry(s.getBytes())
  private implicit def databaseEntryToString(d:DatabaseEntry) = new String(d.getData)

  /** Get the number of items in this DB */
  def size = database.count()

  /** Retrive the stored item for the key, or None if the key isn't mapped. */
  def apply(key:String):Option[String] = {
    debug("Looking up key " + key)
    var value:DatabaseEntry = new DatabaseEntry
    database.get(null,key,value,null) match {
      case OperationStatus.SUCCESS => Some(value)
      case x:OperationStatus => {
        debug("Got " + x + " for " + key)
        None
      }
    }
  }

  /** Add a new mapped key/value pair, as a tuple.
    * <pre>
    * someDatabase += ("key" -&gt; "value")
    * </pre>
    */
  def +=(entry:(String,String)) = {
    debug("Storing " + entry)
    database.put(null,entry._1,entry._2)
    entry
  }

  private var isClosed = false

  def closed = isClosed

  /** close the database safely */
  def close = {
    isClosed = true
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
    if (!dir.exists()) {
      info(dir.getAbsolutePath + " didnt exist, making it...")
      dir.mkdirs()
    }

    val envConfig:EnvironmentConfig = new EnvironmentConfig();
    envConfig.setTransactional(false);
    envConfig.setAllowCreate(true);
    
    val environment = new Environment(dir,envConfig)
    new DB(environment,name)
  }
}
