package shorty

import org.scalatest._
import org.scalatest.matchers._

/** Base class for all tests. */
class BaseTest extends Spec with ShouldMatchers with BeforeAndAfterEach with BeforeAndAfterAll
