package main.scala.aview

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import main.scala.controller.GameStatus
import main.scala.controller.controllerBaseImpl.Controller

class HttpServer(controller: Controller) {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val route: Route = get {
    pathSingleSlash {
      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>HTWG Connect Four</h1>"))
    }
    path("connectfour") {
      complete(HttpEntity(
        ContentTypes.`text/html(UTF-8)`,
        "<h1>HTWG Connect Four</h1>" + controller.gridToHTML
      ))
    } ~
      path("connectfour" / Segment) { command => {
        processInputLine(command)
        complete(
          HttpEntity(ContentTypes.`text/html(UTF-8)`,
            "<h1>HTWG Connect Four</h1>" + controller.currentPlayer() + ", it's your turn" + controller.gridToHTML)
        )
      }
      }
  }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  def unbind = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

  def processInputLine(input: String): Unit = {
    input match {
      case "q" =>
      case "n small" =>
        controller.createEmptyGrid("GridSmall")
      case "n middle" =>
        controller.createEmptyGrid("GridMiddle")
      case "n large" =>
        controller.createEmptyGrid("GridLarge")
      case "undo" => controller.undo
      case "redo" => controller.redo
      case "save" => controller.save
      case "load" => controller.load
      case _ =>
        if (input.startsWith("rename")) {
          controller.renamePlayer(input.replaceAll("rename ", ""))
          return
        }
        if (controller.gameStatus == GameStatus.WIN) {
          println("please start a new game")
          return
        }
        input.toList.filter(c => c != ' ') match {
          case 'i' :: column :: Nil =>
            controller.setValueToBottom(column.asDigit)
          case _ =>
            println("wrong input, repeat your turn!")
        }
    }
  }

}
