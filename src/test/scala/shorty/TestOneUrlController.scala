package shorty

import java.io._

import shorty.db._

class TestOneUrlController extends BaseControllerTest with Logs {

  var hasher:URIHasher = _

  override def beforeEach = {
    val tmpFile = File.createTempFile(getClass.getName,"db")
    tmpFile.delete
    tmpFile.mkdirs
    tmpFile.deleteOnExit
    hasher = URIHasher(DB(tmpFile))
    hasher.start
    hasher !? HashURI("http://www.google.com")
  }

  override def afterEach = hasher.close

  describe("OneUrlController") {
    it ("should respond to get for a URL that is known") {
      val controller = new OneUrlController(hasher,"738ddf")
      val result = controller.get(Map())
      result.getClass should equal (classOf[URL])
      result.asInstanceOf[URL].url should equal ("http://www.google.com")
    }

    it ("should give a 404 for a URL that is not known") {
      val controller = new OneUrlController(hasher,"noway")
      val result = controller.get(Map())
      result.getClass should equal (classOf[Error])
      result.asInstanceOf[Error].httpStatus should equal (404)
    }
    it ("should not respond to post") {
      val controller = new OneUrlController(hasher,"foo")
      shouldNotRespond(controller.post(Map()))
    }
    it ("should not respond to delete") {
      val controller = new OneUrlController(hasher,"foo")
      shouldNotRespond(controller.delete(Map()))
    }
    it ("should not respond to put") {
      val controller = new OneUrlController(hasher,"foo")
      shouldNotRespond(controller.put(Map()))
    }
  }
}
