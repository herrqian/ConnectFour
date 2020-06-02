package aview

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import controller.GridController
import play.api.libs.json.Json
import scala.concurrent.ExecutionContextExecutor


class ModelHttpServer(controller: GridController) {

  implicit val system:ActorSystem = ActorSystem("my-system")
  implicit val materializer:ActorMaterializer = ActorMaterializer()
  implicit  val executionContext: ExecutionContextExecutor = system.dispatcher

  val route:Route =concat(
    post {
      path("grid" / "createEmptyGrid"/ "GridSmall") {
        println("i got a request")
        controller.createEmtpyField("Grid Small")
        complete(HttpEntity(ContentTypes.`application/json`, controller.gridToJson.toString()))
      } ~
        path("grid" / "createEmptyGrid"/ "GridMiddle") {
          controller.createEmtpyField("Grid Middle")
          complete(HttpEntity(ContentTypes.`application/json`, controller.gridToJson.toString()))
        } ~
        path("grid" / "createEmptyGrid"/ "GridLarge") {
          controller.createEmtpyField("Grid Large")
          complete(HttpEntity(ContentTypes.`application/json`, controller.gridToJson.toString()))
        }
    },
    get {
      path("grids") {
        complete(HttpEntity(ContentTypes.`application/json`, controller.gridToJson.toString()))
      }
    }

  )

  val bindingFuture = Http().bindAndHandle(route, "localhost", 11111)

  def unbind = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
