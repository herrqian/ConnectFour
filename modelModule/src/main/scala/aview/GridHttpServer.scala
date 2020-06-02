package aview

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import controller.GridController
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}


class GridHttpServer(controller: GridController) {

  implicit val system:ActorSystem = ActorSystem("my-system")
  implicit val materializer:ActorMaterializer = ActorMaterializer()
  implicit  val executionContext: ExecutionContextExecutor = system.dispatcher

  val route:Route =concat(
    post {
      path("grid" / "createEmptyGrid"/ "GridSmall") {
        println("i got a request")
        controller.createEmtpyField("Grid Small")
        complete(HttpEntity(ContentTypes.`application/json`, Json.obj("event" -> "GridSizeChanged").toString()))
      } ~
        path("grid" / "createEmptyGrid"/ "GridMiddle") {
          controller.createEmtpyField("Grid Middle")
          complete(HttpEntity(ContentTypes.`application/json`, Json.obj("event" -> "GridSizeChanged").toString()))
        } ~
        path("grid" / "createEmptyGrid"/ "GridLarge") {
          controller.createEmtpyField("Grid Large")
          complete(HttpEntity(ContentTypes.`application/json`, Json.obj("event" -> "GridSizeChanged").toString()))
        } ~
        path("valueSetting") {
          decodeRequest {
            entity(as[String]) { data =>
              println(s"what i get from client, $data")
              val mydata = Json.parse(data)
              val col = (mydata \ "col").get.toString.toInt
              val value = (mydata \ "value").get.toString().toInt
              controller.setValueToBottom(col, value) match {
                case Success(row) => {
                  if (controller.checkWinner(row,col,value)) {
                    complete(HttpEntity(ContentTypes.`application/json`, Json.obj("event"-> "WIN").toString()))
                  } else {
                    complete(HttpEntity(ContentTypes.`application/json`,
                      Json.obj("event"-> "CellChanged", "row" -> row,"col" -> col,"value"->value).toString()))
                  }

                }
                case Failure(exception) => {complete(HttpEntity(ContentTypes.`application/json`,
                  Json.obj("event"-> "SetError", "error" -> exception.toString).toString()))}
              }
            }
          }
        } ~
      path("undo") {
        controller.undo
        complete("")
      }~
      path("redo") {
        controller.redo
        complete("")
      }
    },
    get {
      path("grids") {
        complete(HttpEntity(ContentTypes.`application/json`, controller.gridToJson.toString()))
      }~
      path("grids"/"rows") {
        complete(controller.grid.rows.toString)
      } ~
      path("grids"/ "cols") {
        complete(controller.grid.cols.toString)
      }
    },
  )

  val bindingFuture = Http().bindAndHandle(route, "localhost", 11111)

  def unbind = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
