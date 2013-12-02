package dispatch.js
package as

object String extends (Response => String) {
  def apply(r: Response): String = r.responseBody
}
