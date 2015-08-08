package info.golushkov.testforgis.aktors

import akka.actor.Actor
import dispatch.Defaults._
import dispatch._
import play.api.libs.json.{JsArray, JsObject, Json}

case class GisSearch(q: String)

class TwoGisActor extends Actor {
  val key = "ruuxah6217"
  val ver = "1.3"
  val sort = "rating"
  val sites = Seq(
    "Новосибирск",
    "Омск",
    "Томск",
    "Кемерово",
    "Новокузнецк"
  )

  override def receive: Receive = {
    case GisSearch(q) =>
      val valSender = sender()
      sites
        .map(s => url(s"http://catalog.api.2gis.ru/search?key=$key&version=$ver&what=$q&where=$s&sort=$sort"))
        .map(u => Http(u OK as.String))
        .map(
          f => f flatMap {
            res => {
              val firm = (Json.parse(res).as[JsObject] \ "result").as[JsArray].head.as[JsObject]
              val id = (firm \ "id").as[String]
              val hash = (firm \ "hash").as[String]
              val u = url(s"http://catalog.api.2gis.ru/profile?key=$key&version=$ver&id=$id&hash=$hash")
              Http(u OK as.String)
            }
          })
        .map(_.map(f => Seq(f)))
        .fold(Future {Seq()})(
          (a1, a2) => {
            for {
              o <- a1
              t <- a2
            } yield o ++ t
          })
        .map(_.map(Json.parse).map(_.as[JsObject]).map(
          firm => {
            Json.obj(
              "Название" -> (firm \ "name").as[String],
              "Адрес" -> String.format("%s, %s", (firm \ "city_name").as[String], (firm \ "address").as[String]),
              "Рейтинг Фламп" -> (firm \ "rating").asOpt[String].getOrElse("").toString
            )
          }))
        .map(JsArray)
        .onSuccess {
        case res: JsArray =>
          valSender ! Json.stringify(res)
      }
  }
}
