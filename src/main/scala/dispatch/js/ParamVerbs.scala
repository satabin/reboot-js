package dispatch.js

import scala.scalajs.js._

trait ParamVerbs {
  this: Req =>

  def <<(body: Any): Req =
    if(method == "GET")
      copy(method = "POST", body = Some(body))
    else
      copy(body = Some(body))

  def <<(ps: Traversable[(String, String)]): Req =
    if(method == "GET")
      copy(method = "POST", params = params ++ ps)
    else
      copy(params = params ++ ps)

  def <<<(body: Any): Req =
    if(method == "GET")
      copy(method = "PUT", body = Some(body))
    else
      copy(body = Some(body))

  def <<?(qps: Traversable[(String, String)]): Req =
    copy(qparams = qparams ++ qps)

}

