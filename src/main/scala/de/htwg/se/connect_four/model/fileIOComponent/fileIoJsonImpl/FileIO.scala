package de.htwg.se.connect_four.model.fileIOComponent.fileIoJsonImpl

import java.io.{File, PrintWriter}

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor
import com.google.inject.Guice
import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions._
import de.htwg.se.connect_four.ConnectFourModule
import de.htwg.se.connect_four.model.fileIOComponent.FileIOInterface
import de.htwg.se.connect_four.model.gridComponent.{CellInterface, GridInterface}
import play.api.libs.json._

import scala.io.Source
import scala.util.{Failure, Success, Try}

class FileIO extends FileIOInterface {

  override def load: (Option[GridInterface], Array[Boolean]) = {
    val source: String = Source.fromFile("grid.json").getLines.mkString
    val json: JsValue = Json.parse(source)
    val size = (json \ "grid" \ "size").get.toString().toInt
    val rows = (json \ "grid" \ "rows").get.toString.toInt
    val cols = (json \ "grid" \ "cols").get.toString.toInt
    val player1 = (json \ "players" \ "Player1").get.toString().toBoolean
    val player2 = (json \ "players" \ "Player2").get.toString().toBoolean
    val injector = Guice.createInjector(new ConnectFourModule)

    /*  size match {
      case 42 =>  Some(injector.instance[GridInterface](Names.named("Grid Small")))
      case 110 => Some(injector.instance[GridInterface](Names.named("Grid Middle")))
      case 272 => Some(injector.instance[GridInterface](Names.named("Grid Huge")))
      case _ => println("jjj")
    }
    */

    val grid:Option[GridInterface] = size match {
      case 42 => set_grid(json, Some(injector.instance[GridInterface](Names.named("Grid Small"))), rows*cols,0)
      case 110 => set_grid(json, Some(injector.instance[GridInterface](Names.named("Grid Middle"))), rows*cols, 0)
      case 272 => set_grid(json, Some(injector.instance[GridInterface](Names.named("Grid Huge"))), rows*cols, 0)
    }

    /*
     for (index <- 0 until rows * cols) {
       val row = (json \\ "row") (index).as[Int]
       val col = (json \\ "col") (index).as[Int]
       val cell = (json \\ "cell") (index)
       val value = (cell \ "value").as[Int]
       grid = grid.set(row, col, value)
    }
    */
    (grid, Array(player1, player2))
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

  override def save(grid: GridInterface, playerlist: Array[Boolean]): Unit = {
    val pw = new PrintWriter(new File("grid.json"))
    pw.write(Json.prettyPrint(toJson(grid, playerlist)))
    pw.close()
  }

  implicit val cellWrites = new Writes[CellInterface] {
    override def writes(o: CellInterface): JsValue = {
      Json.obj(
        "value" -> o.value
      )
    }
  }

  def toJson(interface: GridInterface, playerlist: Array[Boolean]): JsValue = {
    Json.obj(
      "players" -> Json.obj(
        "Player1" -> playerlist.apply(0),
        "Player2" -> playerlist.apply(1),
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