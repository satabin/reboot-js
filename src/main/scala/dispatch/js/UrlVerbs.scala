package dispatch.js

import scala.scalajs.js.encodeURIComponent

import scala.scalajs.js._

trait UrlVerbs {
  this: Req =>

  def / (segment: String) = {
    val uri = RawUri(url)
    val encodedSegment = encodeURIComponent(segment)
    val rawPath = uri.path.orElse(Some("/")).map {
      case u if u.endsWith("/") => u + encodedSegment
      case u => u + "/" + encodedSegment
    }
    copy(url = uri.copy(path=rawPath).toString)
  }

  def secure = {
    copy(url = RawUri(url).copy(scheme=Some("https")).toString)
  }
}

