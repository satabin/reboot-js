package dispatch.js
package as

import scala.scalajs.js

object Json extends (Response => js.Dynamic) {
  def apply(r: Response): js.Dynamic = js.JSON.parse(r.responseBody)
}
