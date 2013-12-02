package dispatch.js

import org.scalajs.dom._
import scala.scalajs.js._

private object Response {
  val charsetRegex = """\s*charset\s*=\s*(\S+)\s*""".r
}

case class Response(statusCode: Number, headers: Map[String, String], body: Either[String, Blob]) {

  def isSuccess: Boolean =
    statusCode / 100 == 2

  def encoding: String =
    (for {
      tpe <- headers.get("Content-Type")
      charset <- tpe.split(";").collectFirst {
        case Response.charsetRegex(charset) => charset
      }
    } yield charset).orElse(Some("ISO-8859-1")).get

  def mimeType: String =
    headers.get("Content-Type").getOrElse("text/plain")


  def responseBody: String = body match {
    case Left(s)  => s
    case Right(_) => throw new Exception("binary data")
  }

  def responseBodyAsStream: Blob = body match {
    case Left(s)  => new Blob(Array(s.getBytes(encoding)), new BlobPropertyBag { override def `type` = mimeType })
    case Right(b) => b
  }

}

