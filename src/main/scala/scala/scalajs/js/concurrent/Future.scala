package scala.scalajs.js
package concurrent

import scala.util.{
  Try,
  Success,
  Failure
}
import scala.util.control.NonFatal

trait Future[+T] {

  def onComplete[U](func: Try[T] => U): Unit

  def onSuccess[U](pf: PartialFunction[T, U]): Unit = onComplete {
    case Success(v) if pf isDefinedAt v => pf(v)
    case _ =>
  }

  def onFailure[U](callback: PartialFunction[Throwable, U]): Unit = onComplete {
    case Failure(t) if NonFatal(t) && callback.isDefinedAt(t) => callback(t)
    case _ =>
  }

  def isCompleted: Boolean

  def value: Option[Try[T]]

  def map[S](f: T => S): Future[S] = {
    val p = Promise[S]()

    onComplete {
      case result =>
        try {
          result match {
            case Success(r) => p success f(r)
            case f: Failure[_] => p complete f.asInstanceOf[Failure[S]]
          }
        } catch {
          case NonFatal(t) => p failure t
        }
    }

    p.future
  }

  def flatMap[S](f: T => Future[S]): Future[S] = {
    val p = Promise[S]()

    onComplete {
      case f: Failure[_] => p complete f.asInstanceOf[Failure[S]]
      case Success(v) =>
        try {
          f(v).onComplete({
            case f: Failure[_] => p complete f.asInstanceOf[Failure[S]]
            case Success(v) => p success v
          })
        } catch {
          case NonFatal(t) => p failure t
        }
    }

    p.future
  }

  def filter(pred: T => Boolean): Future[T] = {
    val p = Promise[T]()

    onComplete {
      case f: Failure[_] => p complete f.asInstanceOf[Failure[T]]
      case Success(v) =>
        try {
          if (pred(v)) p success v
          else p failure new NoSuchElementException("Future.filter predicate is not satisfied")
        } catch {
          case NonFatal(t) => p failure t
        }
    }

    p.future
  }

  /** Used by for-comprehensions.
   */
  final def withFilter(p: T => Boolean): Future[T] = filter(p)

  def foreach[U](f: T => U): Unit = onComplete {
    case Success(r) => f(r)
    case _  => // do nothing
  }

}

