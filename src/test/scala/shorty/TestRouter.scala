package shorty

class TestRouter extends BaseTest {

  describe("Router") {
    it ("should route root to AllUrlsController") {
      val router = new AnyRef with Router

      router.route(Nil).get.getClass should equal (classOf[AllUrlsController])
      router.route(List()).get.getClass should equal (classOf[AllUrlsController])
    }
    
    it ("should route all other requests to a OneUrlController") {
      val router = new AnyRef with Router
      
      val controller = router.route(List("foo")).get
      controller.getClass should equal (classOf[OneUrlController])
      controller.asInstanceOf[OneUrlController].hash should equal ("foo")
    }

    it ("should ignore anything after the hash") {
      val router = new AnyRef with Router
      
      val controller = router.route(List("foo","bar","baz","blah")).get
      controller.getClass should equal (classOf[OneUrlController])
      controller.asInstanceOf[OneUrlController].hash should equal ("foo")
    }
  }
}
