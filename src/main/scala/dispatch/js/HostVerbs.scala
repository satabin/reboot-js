package dispatch.js

import scala.scalajs.js.encodeURI

trait HostVerbs {
  def apply(host: String) =
    Req(encodeURI(s"http://$host/"))

  def apply(host: String, port: Int) =
    Req(encodeURI(s"http://$host:$port/"))
}

