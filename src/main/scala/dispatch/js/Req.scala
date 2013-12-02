package dispatch.js

import scala.scalajs.js._

object :/ extends HostVerbs
object url extends HostVerbs
object host extends HostVerbs

case class Req private[js](
  url: String,
  method: String = "GET",
  headers: Traversable[(String, String)] = Traversable(),
  body: Option[Any] = None,
  params: Traversable[(String, String)] = Traversable(),
  qparams: Traversable[(String, String)] = Traversable(),
  userPassword: Option[(String, String)] = None
) extends MethodVerbs with UrlVerbs with ParamVerbs with AuthVerbs with HeaderVerbs {

  def >[T](f: Response => T): (Req, Response => T) =
    (this, f)

}

