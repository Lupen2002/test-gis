package info.golushkov.testforgis

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import akka.util.Timeout
import spray.can.Http

import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import scala.language.postfixOps

import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global

object Boot extends App {
  for{
    a <- Future{Seq("")}
    b <- Future{Seq("")}
  } yield a ++ b

  implicit val timeout = Timeout(5 seconds)

  implicit val system = ActorSystem("2gis-test")

  val service = system.actorOf(Props[RestService], "RestService")
  IO(Http) ? Http.Bind(service, interface = "0.0.0.0", port = 8080)

}
