package shorty

class TestRouter extends BaseTest {

  var router:Router = _

  override def beforeEach = router = new AnyRef with Router {
    def uriHasher = null
    def apiKey = "foo"
  }
  describe("Router") {
    it ("should route root to AllUrlsController") {
      router.route(Nil).get.getClass should equal (classOf[AllUrlsController])
      router.route(List()).get.getClass should equal (classOf[AllUrlsController])
    }
    
    it ("should route all other requests to a OneUrlController") {
      val controller = router.route(List("foo")).get
      controller.getClass should equal (classOf[OneUrlController])
      controller.asInstanceOf[OneUrlController].hash should equal ("foo")
    }

    it ("should ignore anything after the hash") {
      val controller = router.route(List("foo","bar","baz","blah")).get
      controller.getClass should equal (classOf[OneUrlController])
      controller.asInstanceOf[OneUrlController].hash should equal ("foo")
    }
  }
}
