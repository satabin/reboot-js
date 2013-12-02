package scala.scalajs.js
package concurrent
package impl

import org.scalajs.dom._

import scala.util.Try
import scala.collection.mutable.ListBuffer

class SimplePromise[T] extends Promise[T] with Future[T] {

  private[this] var result: Option[Try[T]] = None
  private[this] val callbacks = ListBuffer.empty[Try[T] => _]

  def future = this

  def isCompleted: Boolean =
    result.isDefined

  def onComplete[U](func: Try[T] => U): Unit = result match {
    case Some(v) =>
      // result already set, call the callback
      func(v)
    case None =>
      // no result yet, register the call back
      callbacks += func
  }

  def value: Option[Try[T]] =
    result

  def tryComplete(result: Try[T]): Boolean =
    if(isCompleted) {
      false
    } else {
      window.setTimeout(() => {
        this.result = Some(result)
        for(cb <- callbacks)
          window.setTimeout(() => cb(result), 0)
      }, 0)
      true
    }

}

