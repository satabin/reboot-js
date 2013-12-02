package dispatch.js

import scala.scalajs.js._

trait AuthVerbs {
  this: Req =>

  def as(user: String, password: String): Req =
    copy(userPassword = Some(user -> password))

}

