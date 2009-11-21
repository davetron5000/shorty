package shorty

import java.io._

import shorty.db._

class TestAllUrlsController extends BaseControllerTest with Logs {

  var controller:AllUrlsController = _
  var hasher:URIHasher = _

  override def beforeEach = {
    val tmpFile = File.createTempFile(getClass.getName,"db")
    tmpFile.delete
    tmpFile.mkdirs
    tmpFile.deleteOnExit
    hasher = URIHasher(DB(tmpFile))
    hasher.start
    controller = new AllUrlsController(hasher)
  }

  override def afterEach = hasher.close

  describe("AllUrlsController") {
    it ("should respond to post") {
      val result = controller.post(Map("url" -> "http://www.google.com"))
      result.getClass should equal (classOf[Hash])
      result.asInstanceOf[Hash].hash should equal ("738ddf")
    }
    it ("should respond get the same result for the same url") {
      var result = controller.post(Map("url" -> "http://www.google.com"))
      val hash = result.asInstanceOf[Hash].hash

      result = controller.post(Map("url" -> "http://www.google.com"))
      result.getClass should equal (classOf[Hash])
      result.asInstanceOf[Hash].hash should equal (hash)
    }
    it ("should not respond to get") {
      shouldNotRespond(controller.get(Map()))
    }
    it ("should not respond to put") {
      shouldNotRespond(controller.put(Map()))
    }
    it ("should not respond to delete") {
      shouldNotRespond(controller.delete(Map()))
    }
  }
}
