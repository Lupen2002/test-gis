package info.golushkov.testforgis

import akka.actor._
import akka.actor.Actor.Receive
import akka.util.Timeout
import info.golushkov.testforgis.aktors.{GisSearch, TwoGisActor}
import spray.http.HttpEntity

import spray.http.HttpMethods._
import spray.http._
import spray.routing.HttpService

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.postfixOps
import scala.util.Success
import akka.pattern.{ask, pipe}

import scala.concurrent.duration._
import scala.language.postfixOps

import scala.concurrent.ExecutionContext.Implicits.global

class RestService extends Actor with TestRestService {
  def actorRefFactory = context

  def receive = runRoute(route)

}

trait TestRestService extends HttpService {
  implicit val timeout = Timeout(15 seconds)
  val gis = actorRefFactory.actorOf(Props[TwoGisActor])

  val route = {
    path("search") {
      get {
        parameterMap { parameters =>
          onSuccess({
            gis ? GisSearch(parameters("q"))
          }) { res =>
            complete {
              res.toString
            }
          }
        }
      }
    }
  }
}
