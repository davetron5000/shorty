Shorty is a url shortening application written in Scala.

It's written in Scala because I wanted a to write a small, but functional, application in Scala.  Plus, I have <a href="http://&#10106;&#10144;.ws/">&#10106;&#10144;.ws/g/1</a> registered as my own personal url shortener.

# Building

 * `cp src/main/webapp/WEB-INF/web.xml.dev src/main/webapp/WEB-INF/web.xml.prod`
 * Edit `web.xml.prod` with:
   * Full path to writable directory where you will store your database
   * the name of your host for shortening, 
   * secret key to keep others from using it
 * `./prod`
 * `sbt package`
 * on your deployment server
 * `scp package/shorty-1.0.war` to your J2EE container to deploy it

# Using

 `POST` to `http://yourdomain.com/context/?url=URL_TO_SHORTEN&api=API_KEY`

The result will be the shortened URL. A `GET` to this URL will redirect you.  If you `GET` using an `Accept:` header of `text/xml` or `application/json`, you will get
XML or JSON instead.  You can also specify `_type` as a request parameter to override the `Accept:` header

