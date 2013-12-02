package dispatch.js

trait MethodVerbs {
  this: Req =>

  def DELETE: Req =
    copy(method = "DELETE")
  def GET: Req =
    copy(method = "GET")
  def HEAD: Req =
    copy(method = "HEAD")
  def OPTIONS: Req =
    copy(method = "OPTIONS")
  def PATCH: Req =
    copy(method = "PATCH")
  def POST: Req =
    copy(method = "POST")
  def PUT: Req =
    copy(method = "PUT")
  def TRACE: Req =
    copy(method = "TRACE")
}
