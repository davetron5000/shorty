package shorty

class BaseControllerTest extends BaseTest {
  protected def shouldNotRespond(result: ControllerResponse) = {
    result.getClass should equal (classOf[Error])
    result.asInstanceOf[Error].httpStatus should equal (405)
  }
}
