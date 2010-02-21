Shorty is a RESTful url shortening application written in Scala.

# Building

 * `cp src/main/webapp/WEB-INF/web.xml.dev src/main/webapp/WEB-INF/web.xml.prod`
 * Edit `web.xml.prod` with:
   * Full path to writable directory where you will store your database
   * the name of your host for shortening, 
   * secret key to keep others from using it
 * `./prod # Copies your web.xml.prod to web.xml`
 * `sbt package`
 * on your deployment server, create the database directory
 * `scp package/shorty-1.0.war` to your J2EE container to deploy it

# Using

 * `POST` to `http://yourdomain.com/context/?url=URL_TO_SHORTEN&api=API_KEY`
   * Returns the shortened URL
   * If you request `text/xml` or `application/json`, you will get an XML or JSON version
   * Request alternate formats via the `Accept:` header, or via the `_type` request parameter

# Why?

 * Wanted to write a real-world Scala app I could use
 * Wanted to write my own shortener for my domain
 * Wanted to write something that could use Scala actors
 * Wanted to learn SBT some

# Conclusions

[Read my blog entry on it](http://&#10106;&#10144;.ws/s/86c53f), or just skim this list for the lazy:

 * Scala is a fun and concise language
 * Bridging un-genericized classes (e.g. `java.util.Enumeration`) to Scala is painful
 * ScalaTest is reasonably cool
 * SBT is *so close* to being awesome, and is better than Maven, but still not that great
 * J2EE packaging and deploying *sucks*
 * Scala makes things so much easier than the Java equivalents.
