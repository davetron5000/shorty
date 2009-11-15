package shorty

class TestAllUrlsController extends BaseControllerTest {

  var controller:AllUrlsController = _

  override def beforeEach() = controller = new AllUrlsController

  describe("AllUrlsController") {
    it ("should respond to post") {
      controller.post.isLeft should equal (false)
      controller.post.isRight should equal (true)
    }
    it ("should not respond to get") {
      shouldNotRespond(controller.get)
    }
    it ("should not respond to put") {
      shouldNotRespond(controller.get)
    }
    it ("should not respond to delete") {
      shouldNotRespond(controller.get)
    }
  }
}
