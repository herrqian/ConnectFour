package main.scala.controller.controllerBaseImpl

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest}
import akka.http.scaladsl.unmarshalling._
import akka.stream.ActorMaterializer
import com.google.inject.Inject
import controller._
import main.scala.controller.GameStatus.{IDLE, WIN}
import play.api.libs.json.{JsValue, Json}


import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor}


class Controller @Inject() extends ControllerInterface {

  var gameStatus = IDLE
  //val injector = Guice.createInjector(new ConnectFourModule)
  //val fileIo = new FileIO
  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  def createEmptyGrid(s: String): Unit = {
    val url = "http://localhost:11111/grid/createEmptyGrid/" + s
    Http().singleRequest(Post(url))
    this.resetPlayerList()
    gameStatus = IDLE
    publish(new GridSizeChanged(s))
  }

  def setValueToBottom(col:Int):Unit = {
    val url = "http://localhost:11111/valueSetting"
    val data = Json.obj("col" -> col,
    "value"->getValueOfPlayer)
    val response = Http().singleRequest(HttpRequest(
      method = HttpMethods.POST,
      uri = url,
      entity = HttpEntity(ContentTypes.`application/json`, data.toString())
    ))
    val jsonStringFuture = response.flatMap(r => Unmarshal(r.entity).to[String])
    val jsonString = Await.result(jsonStringFuture, Duration(1, TimeUnit.SECONDS))
    val json = Json.parse(jsonString)
    (json \ "event").get.toString().replace("\"","") match {
      case "CellChanged" => {
        val row = (json \ "row").get.toString().toInt
        val col = (json \ "col").get.toString().toInt
        val value = (json \ "value").get.toString().toInt
        changeTurn()
        publish(new CellChanged(row,col,value))
      }
      case "WIN" => publish(new WinEvent)
      case "SetError" => publish(new SaveError((json \ "error").get.toString()))
    }
  }

  def getValueOfPlayer:Int = {
    val url = "http://localhost:22222/values"
    val response = Http().singleRequest(Get(url))
    val jsonStringFuture = response.flatMap(r => Unmarshal(r.entity).to[String])
    val jsonString = Await.result(jsonStringFuture, Duration(1, TimeUnit.SECONDS))
    jsonString.toInt
  }

  def getGrid:JsValue = {
    val url = "http://localhost:11111/grids"
    val response = Http().singleRequest(Get(url))
    val jsonStringFuture = response.flatMap(r => Unmarshal(r.entity).to[String])
    val jsonString = Await.result(jsonStringFuture, Duration(1, TimeUnit.SECONDS))
    Json.parse(jsonString)
    //printToHTML(json)
  }

  def printToHTML(jsValue: JsValue):String = {
    val rows = (jsValue \ "rows").get.toString.toInt
    val cols = (jsValue \ "cols").get.toString.toInt
    val cellsarray = Array.ofDim[Int](rows, cols)
    for (index <- 0 until rows * cols) {
      val row = (jsValue \\ "row") (index).as[Int]
      val col = (jsValue \\ "col") (index).as[Int]
      val cell = (jsValue \\ "cell") (index)
      val value = (cell \ "value").as[Int]
      cellsarray(row)(col) = value
    }
    var htmltext = ""

    cellsarray.foreach(
      row => {
        htmltext += row.fold("<p style=\"font-family:'Lucida Console', monospace\">")(_ + " " + _)  + "</p>"
      }
    )
    htmltext
  }

  def printToString(jsValue: JsValue):String = {
    val rows = (jsValue \ "rows").get.toString.toInt
    val cols = (jsValue \ "cols").get.toString.toInt
    val cellsarray = Array.ofDim[Int](rows, cols)
    for (index <- 0 until rows * cols) {
      val row = (jsValue \\ "row") (index).as[Int]
      val col = (jsValue \\ "col") (index).as[Int]
      val cell = (jsValue \\ "cell") (index)
      val value = (cell \ "value").as[Int]
      cellsarray(row)(col) = value
    }

    var text = ""
    cellsarray.foreach(
      row => {
        text += row.fold("")(_ + " " + _) + System.lineSeparator()
      }
    )
    text
  }

  def changeTurn(): Unit = {
    val url = "http://localhost:22222/players/reserving"
    Http().singleRequest(Post(url))
  }

  def currentPlayer():String = {
    val url = "http://localhost:22222/players"
    val response = Http().singleRequest(Get(url))
    val jsonStringFuture = response.flatMap(r => Unmarshal(r.entity).to[String])
    val jsonString = Await.result(jsonStringFuture, Duration(1, TimeUnit.SECONDS))
    val json = Json.parse(jsonString)
    (json \ "player1").get.toString().replace("\"","")
  }

  def resetPlayerList(): Unit = {
    val url = "http://localhost:22222/players/resetting"
    Http().singleRequest(Post(url))
  }

  def gridToString: String = printToString(getGrid)

  def gridToHTML: String = printToHTML(getGrid)

  def undo: Unit = {
    Http().singleRequest(Post("http://localhost:11111/undo"))
    this.changeTurn()
    publish(new GridChanged)
  }

  def redo: Unit = {
    Http().singleRequest(Post("http://localhost:11111/redo"))
    this.changeTurn()
    publish(new GridChanged)
  }

  override def renamePlayer(newname: String): Unit = {
    val url = "http://localhost:22222/players/rename"
    val data = Json.obj({"name" -> newname})
    Http().singleRequest(Post(url, HttpEntity(ContentTypes.`application/json`, data.toString())))
  }

  override def getGameStatus() = gameStatus

  override def getGridRow: Int = {
    val url = "http://localhost:22222/grids/rows"
    val response = Http().singleRequest(Get(url))
    val jsonStringFuture = response.flatMap(r => Unmarshal(r.entity).to[String])
    val jsonString = Await.result(jsonStringFuture, Duration(1, TimeUnit.SECONDS))
    jsonString.toInt
  }

  override def getGridCol: Int = {
    val url = "http://localhost:22222/gird/cols"
    val response = Http().singleRequest(Get(url))
    val jsonStringFuture = response.flatMap(r => Unmarshal(r.entity).to[String])
    val jsonString = Await.result(jsonStringFuture, Duration(1, TimeUnit.SECONDS))
    jsonString.toInt
  }

  override def save: Unit = {}

  override def load: Unit = {}

//  def save(): Unit = {
//    fileIo.save(grid, playerlist) match {
//      case Success(_) => publish(new GridChanged)
//      case Failure(exception) => publish(new SaveError(exception.toString))
//    }
//  }
//
//  def load(): Unit = {
//    fileIo.load match {
//      case Success(data) => {
//        grid = data._1 match {
//          case Some(g) => g
//        }
//        playerlist = data._2
//        publish(new GridSizeChanged("new size"))
//        publish(new GridChanged)
//      }
//      case Failure(exception) => publish(new LoadError(exception.toString))
//    }
//  }

//  def setValueToBottom(column: Int): Unit = {
//    val value = if (playerlist(0).name.equals(player1.name)) {
//      1
//    } else {
//      2
//    }
//    this.setValueR(grid.col(column), grid.cells.row - 1, column, value) match {
//      case Success(row) => {
//        if (this.checkWinner(row, column, value)) {
//          gameStatus = WIN
//          publish(new WinEvent(value))
//        } else {
//          this.changeTurn()
//          println(playerlist)
//          publish(new CellChanged(row, column, value))
//        }
//      }
//      case Failure(exception) => publish(new SetError(exception.toString))
//    }
//
//  }
//
//  private def setValueR(cells: Field, row: Int, col: Int, stone: Int): Try[Int] = {
//    Try(cells.cell(row).equals(Cell(0))) match {
//      case Success(cond) => {
//        if (cond) {
//          undoManager.doStep(new SetCommand(row, col, stone, this))
//          Success(row)
//        } else {
//          setValueR(cells, row - 1, col, stone)
//        }
//      }
//      case Failure(exception) => Failure(exception)
//    }
//  }

//  def checkWinner(row: Int, col: Int, stone: Int): Boolean = {
//    grid.is4Stone(row, col, stone)
//  }
}
