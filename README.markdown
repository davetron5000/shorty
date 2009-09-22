Shorty is a url shortening application

 * `GET /` - lists all shortened urls
 * `POST /` - creates a new shortened url
 * `GET /name` - gets the url shortened to nasme
   * `text/html` - sends a 301 to redirect
   * `text/xml` - returns XML describing the new URL
   * `text/json` returns JSON describing the new URL
 * `PUT /name` - puts a new url to a named shortening
 * `DELETE /name` - removes a shortening

