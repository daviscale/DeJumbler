package org.daviscale.dejumbler.api

import spray.json.DefaultJsonProtocol._
import spray.json.PrettyPrinter

case class DejumbleResponse(candidates: Seq[String])

object JsonFormats {
  implicit val printer = PrettyPrinter
  implicit val statusMessageFormat = jsonFormat1(DejumbleResponse)
}
