package scala.de.htwg.se.connect_four.module.fileIOComponent.fileIoJsonImpl

import java.io.{File, PrintWriter}

import PlayerModul.{Player, PlayerInterface}
import com.google.inject.Guice
import com.google.inject.name.Names
import main.scala.model.gridComponent.gridAdvancedImpl.Grid
import main.scala.model.gridComponent.{CellInterface, GridInterface}
import play.api.libs.json.{JsNumber, JsValue, Json, Writes}

import scala.de.htwg.se.connect_four.module.ConnectFourModule
import scala.de.htwg.se.connect_four.module.fileIOComponent.FileIOInterface
import scala.io.Source
import scala.util.{Failure, Success, Try}

class FileIO extends FileIOInterface {
  final val FILE_NAME = "grid.json"

  override def load: Try[(Option[GridInterface], Array[Player])] = {
    Try {
      val source: String = Source.fromFile(FILE_NAME).getLines.mkString
      val json: JsValue = Json.parse(source)
      val size = (json \ "grid" \ "size").get.toString().toInt
      val rows = (json \ "grid" \ "rows").get.toString.toInt
      val cols = (json \ "grid" \ "cols").get.toString.toInt
      val player1 = Player((json \ "players" \ "Player1").get.toString())
      val player2 = Player((json \ "players" \ "Player2").get.toString())

      val grid: Option[GridInterface] = size match {
        case 42 => set_grid(json, Some(new Grid(6,7)), rows * cols, 0)
        case 110 => set_grid(json, Some(new Grid(10,11)), rows * cols, 0)
        case 272 => set_grid(json, Some(new Grid(16,17)), rows * cols, 0)
      }
      (grid, Array(player1, player2))
    }
  }

   def set_grid(json: JsValue, grid: Option[GridInterface],prod:Int, index:Int): Option[GridInterface] = {

    if(index != prod){

      val row = (json \\ "row") (index).as[Int]
      val col = (json \\ "col") (index).as[Int]
      val cell = (json \\ "cell") (index)
      val value = (cell \ "value").as[Int]
      Try(grid) match {

        case Success(g) => set_grid(json, Some(g.get.set(row, col, value)), prod, index+1)
        case Failure(_)  => None
      }

    } else {
      grid
    }

  }

  override def save(grid: GridInterface, playerlist: Array[Player]): Try[Unit] = {
    Try {
      val pw = new PrintWriter(new File(FILE_NAME))
      pw.write(Json.prettyPrint(toJson(grid, playerlist)))
      pw.close()
    }
  }

  implicit val cellWrites = new Writes[CellInterface] {
    override def writes(o: CellInterface): JsValue = {
      Json.obj(
        "value" -> o.value
      )
    }
  }

  def toJson(interface: GridInterface, playerlist: Array[Player]): JsValue = {
    Json.obj(
      "players" -> Json.obj(
        "Player1" -> playerlist.apply(0).toString,
        "Player2" -> playerlist.apply(1).toString,
      ),
      "grid" -> Json.obj(
        "size" -> JsNumber(interface.rows * interface.cols),
        "rows" -> JsNumber(interface.rows),
        "cols" -> JsNumber(interface.cols),
        "cells" -> Json.toJson(
          for {
            row <- 0 until interface.rows
            col <- 0 until interface.cols
          } yield {
            Json.obj(
              "row" -> row,
              "col" -> col,
              "cell" -> Json.toJson(interface.cell(row, col))
            )
          }
        )
      )
    )
  }

}
