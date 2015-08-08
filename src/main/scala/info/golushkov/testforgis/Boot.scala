package info.golushkov.testforgis

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import spray.can.Http

import scala.concurrent.duration._
import scala.language.postfixOps

object Boot extends App {

  implicit val timeout = Timeout(5 seconds)

  implicit val system = ActorSystem("2gis-test")

  val service = system.actorOf(Props[RestService], "RestService")
  IO(Http) ? Http.Bind(service, interface = "0.0.0.0", port = 8080)

}
