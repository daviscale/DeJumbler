package org.daviscale.dejumbler.api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AsyncFlatSpec

import scala.concurrent.Future

class DejumblerServerIntegrationSpec extends AsyncFlatSpec with BeforeAndAfterAll{

  implicit val actorSystem: ActorSystem = ActorSystem("DejumblerServerIntegrationSpec")

  override def beforeAll(): Unit = {
    // future is needed so that the server doesn't block the execution of the tests
    Future { DejumblerServer.main(Array.empty) }
  }

  override def afterAll(): Unit = {
    DejumblerServer.stopServer()
  }

  import JsonFormats._

  "The Dejumbler server" should "unscramble words that are 'jumbled'" in {
    Http()
      .singleRequest(Post("http://localhost:8080/dejumble", "lwotau"))
      .flatMap { response => Unmarshal(response).to[DejumbleResponse].map(_.words)}
      .map { words =>
        assert(words.size == 1)
        assert(words.head == "outlaw")
      }
  }
}
