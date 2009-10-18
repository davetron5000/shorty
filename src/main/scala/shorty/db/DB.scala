package shorty.db

import java.io._

import com.sleepycat.je._

class DB(env:Environment) extends Logs {
  val database = {
    val config = new DatabaseConfig
    config.setAllowCreate(true)
    env.openDatabase(null,"shorty",config)
  }

  private implicit def stringToDatabaseEntry(s:String) = new DatabaseEntry(s.getBytes())

  def size = database.count()
  def get(key:String) = {
    var value:DatabaseEntry = new DatabaseEntry
    database.get(null,key,value,null) match {
      case OperationStatus.SUCCESS => Some(new String(value.getData))
      case x:OperationStatus => {
        debug("Got " + x + " for " + key)
        None
      }
    }
  }

  def put(key:String, value:String) = {
    database.put(null,key,value)
  }

  def close = {
    database.close()
    env.close()
  }

  override def finalize = close
}

object DB extends Logs {
  def apply(dir:File) = {
    debug("Creating new DB in " + dir)
    if (!dir.exists()) dir.mkdirs()

    val envConfig:EnvironmentConfig = new EnvironmentConfig();
    envConfig.setTransactional(false);
    envConfig.setAllowCreate(true);
    
    val environment = new Environment(dir,envConfig)
    new DB(environment)
  }
}
