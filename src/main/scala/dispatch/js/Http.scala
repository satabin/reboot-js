package dispatch.js

import scala.scalajs.js.concurrent._

import scala.language.existentials

case class Http(client: AsyncHttpClient[_]) {

  def apply[T](request: Req, handler: Response => T): Future[T] =
    client(request, handler)

  def apply[T](pair: (Req, Response => T)): Future[T] =
    apply(pair._1, pair._2)

  def apply(req: Req): Future[Response] =
    apply(req, identity)

}
