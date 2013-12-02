package example

import dispatch.js._
import scala.scalajs.js._
import org.scalajs.dom._

object RebootExample {

  val http = new Http(client.XMLHttpRequestClient)

  def click(): Boolean = {

    val time_country = for {
      time <- http(:/("http://www.timeapi.org") / "utc" / "now" > as.String)
      //country <- http(:/("http://freegeoip.net/") / "json" > as.Json)
    //} yield (time, country)
    } yield time

    /*for((time, _) <- time_country) {
      document.getElementById("time").innerHTML = time
      console.log(time)
    }

    for((_, country) <- time_country) {
      document.getElementById("country").innerHTML = JSON.stringify(country)
      console.log(country)
    }*/

   true

  }

  def main() = {

    window.document.getElementById("gettc").onclick = { (e: MouseEvent) =>
      this.click()
    }

  }

}
