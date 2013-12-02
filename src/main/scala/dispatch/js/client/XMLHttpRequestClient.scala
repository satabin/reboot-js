package dispatch.js
package client

import scala.scalajs.js._
import Any._
import concurrent._
import org.scalajs.dom._

import scala.util.{
  Try,
  Success,
  Failure
}
import scala.util.control.NonFatal
import scala.collection.mutable.ListBuffer

/** Implementation of Http client that uses directly `XMLHttpRequest`.
 *
 *  @author Lucas Satabin
 */
object XMLHttpRequestClient extends AsyncHttpClient[XMLHttpRequest] {

  protected def create: XMLHttpRequest =
    new XMLHttpRequest{}

  def apply[T](request: Req, handler: Response => T): Future[T] = {

    val xhr = new XMLHttpRequest{}

    val fut = new XMLHttpRequestFuture(xhr, handler)

    val body: Option[Any] = (request.body, request.params) match {
      case (Some(body), _)                => Some(body)
      case (_, params) if params.nonEmpty => Some(params.map{ case (k, v) => s"$k=$v" }.mkString("\n"))
      case (None, _)                      => None
    }

    // set the header
    for((key, value) <- request.headers)
      xhr.setRequestHeader(key, value)

    // build the query parameter string if any
    val query = request.qparams.map { case (k, v) => s"$k=$v" }.mkString("&") match {
      case "" => None
      case s  => Some(s)
    }
    val uri = RawUri(request.url).copy(query = query).toString

    // open the request
    request.userPassword match {
      case Some((user, password)) => xhr.open(request.method, uri, true, user, password)
      case None                   => xhr.open(request.method, uri, true)
    }

    // and finally send it with optional content
    body match {
      case Some(body) => xhr.send(body)
      case None       => xhr.send()
    }

    fut

  }

}

private class XMLHttpRequestFuture[T](xhr: XMLHttpRequest, handler: Response => T) extends Future[T] {

  import org.scalajs.dom._

  private[this] var result: Option[Try[T]] = None
  private[this] val callbacks = ListBuffer.empty[Try[T] => _]

  xhr.onreadystatechange = onStateChange _

  private def onStateChange(event: Event) {
    val self = event.target.asInstanceOf[XMLHttpRequest]
    if(isCompleted) {
      val headers =
        (for(line <- toArray(self.getAllResponseHeaders.split("\n")))
          yield {
            val re = new RegExp("([^:]+):(.*)")
            val a = re.exec(line)
           (a(0), a(1))
          }).toMap
      val res = Try {
        String.toScalaString(self.responseType) match {
          case "" | "text" =>
            // by default it is a string
            handler(Response(self.status, headers, Left(self.responseText)))
          case "blob" =>
            // it can be a blob
            handler(Response(self.status, headers, Right(self.response.asInstanceOf[Blob])))
          case s =>
            throw new Exception(s"unknown result type $s")
        }
      }

      val result = res

      // execute the callbacks
      for(cb <- callbacks)
        window.setTimeout(() => cb(res), 0)

    }
  }

  def isCompleted: Boolean =
    xhr.readyState == XMLHttpRequest.DONE

  def value: Option[Try[T]] =
    result

  def onComplete[U](func: Try[T] => U): Unit = result match {
    case Some(v) =>
      // result already set, call the callback
      func(v)
    case None =>
      // no result yet, register the call back
      callbacks += func
  }

}

