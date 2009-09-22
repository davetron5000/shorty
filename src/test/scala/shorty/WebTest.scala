package shorty

import net.sourceforge.jwebunit.junit._

/** Base class for all web tests; this sets up the web
 * tester instance that can be used to test pages
 */
class WebTest extends BaseTest {
  protected[this] var tester:WebTester = _
  
  override def beforeEach = tester = new WebTester
}
