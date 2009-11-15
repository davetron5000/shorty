package shorty

class TestAllUrlsController extends BaseTest {

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

  private def shouldNotRespond(result: Either[(Int,String),String]) = {
    result.isLeft should equal (true)
    result.isRight should equal (false)
    result.left.get._1 should equal (405)
  }

}
