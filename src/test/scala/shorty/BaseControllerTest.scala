package shorty

class BaseControllerTest extends BaseTest {
  protected def shouldNotRespond(result: Either[(Int,String),String]) = {
    result.isLeft should equal (true)
    result.isRight should equal (false)
    result.left.get._1 should equal (405)
  }
}
