package dispatch.js

import scala.scalajs.js._

trait HeaderVerbs {
  this: Req =>

  def <:<(hs: Traversable[(String, String)]): Req =
    copy(headers = headers ++ hs)

}

