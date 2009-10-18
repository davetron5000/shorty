package shorty.db

class URIHasher(database:DB) extends Logs {
  def size = database.size
  def close = database.close
}
