package org.daviscale.dejumbler
package api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

object DejumblerServer {
  import JsonFormats._

  implicit val system: ActorSystem = ActorSystem("customer-management-system")
  // needed for the future flatMap/onComplete in the stopServer method
  implicit val executionContext: ExecutionContext = system.dispatcher

  var bindingFuture: Future[ServerBinding] = _

  // timeout for the POST request to return
  implicit val timeout: Timeout = 10.seconds

  def main(args: Array[String]): Unit = {
    val route = {
      path("dejumble") {
        post {
          entity(as[String]) { jumbledWord =>
            val future = Dejumbler
              .findCandidatesBruteForce(jumbledWord)
              .map(DejumbleResponse)
            complete(future)
          }
        }
      }
    }

    bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

    println(s"Server online at http://localhost:8080/")
  }


  def stopServer() {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
