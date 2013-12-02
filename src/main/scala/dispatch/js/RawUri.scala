package dispatch.js

import scala.scalajs.js.RegExp

case class RawUri(
  scheme: Option[String],
  userInfo: Option[String],
  host: Option[String],
  port: Option[String],
  path: Option[String],
  query: Option[String],
  fragment: Option[String]
) {
  override def toString =
    (scheme.map { _ + ":" } ::
     Some("//") ::
     userInfo.map { _ + "@" } ::
     host ::
     port.map { ":" + _ } ::
     path ::
     query.map { "?" + _ } ::
     fragment.map { "#" + _ } ::
     Nil
   ).flatten.mkString
}

object RawUri {

  val regexp =
    RegExp("""^(((([^:\/#\?]+:)?(?:(\/\/)((?:(([^:@\/#\?]+)(?:\:([^:@\/#\?]+))?)@)?(([^:\/#\?\]\[]+|\[[^\/\]@#?]+\])(?:\:([0-9]+))?))?)?)?((\/?(?:[^\/\?#]+\/+)*)([^\?#]*)))?(\?[^#]+)?)(#.*)?""")

  def apply(str: String): RawUri = {
    val matches = regexp.exec(str)
    new RawUri(
      Option(matches(4)),
      Option(matches(7)),
      Option(matches(11)),
      Option(matches(12)), //.map(_.asInstanceOf[Int]),
      Option(matches(13)),
      Option(matches(16)),
      Option(matches(17))
    )
  }
}

