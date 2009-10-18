package shorty.db

import shorty._

class TestDB extends BaseTest {

  var database:DB = _

  override def beforeEach = {
    database = new DB()
  }

  override def afterEach = { 
  }

  describe("DB") {
    it ("should initially be empty") {
      database.size should equal(0)
    }

    it ("should return None on get of unknown key") {
      database.get("nevertobeused") should equal(None)
    }
  }

}
