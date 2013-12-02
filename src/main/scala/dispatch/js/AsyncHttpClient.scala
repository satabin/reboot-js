package dispatch.js

import scala.scalajs.js.concurrent._

/** Interface to the underlying asynchronous Http client implementation.
 *  Implement this trait to add a binding to your favorite client
 *
 *  @author Lucas Satabin
 */
trait AsyncHttpClient[Requestor] {

  protected def create: Requestor

  def apply[T](request: Req, handler: Response => T): Future[T]

}

