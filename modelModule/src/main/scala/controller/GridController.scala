package controller

import aview.GridModule
import com.google.inject.{Guice, Injector}
import main.scala.model.gridComponent.GridInterface
import main.scala.model.gridComponent.gridAdvancedImpl.Grid
import main.scala.model.gridComponent.gridBaseImpl.{Cell, Field}
import model.daoComponent.DAOInterface
import model.daoComponent.slickImpl.SlickDao
import play.api.libs.json.{JsNumber, JsObject, Json}
import util.UndoManager

import scala.util.{Failure, Success, Try}

class GridController(var grid: GridInterface) {

  val injector: Injector = Guice.createInjector(new GridModule)
  private val undoManager = new UndoManager
  var db: DAOInterface = new SlickDao

  object Grids extends Enumeration {
    type Grids = Value
    val small = Value("Grid Small").toString
    val middle = Value("Grid Middle").toString
    val large = Value("Grid Large").toString
  }

  def createEmtpyField(size: String): Unit = {
    size match {
      case Grids.small =>
        grid = new Grid(6, 7)
      case Grids.middle =>
        grid = new Grid(10, 11)
      case Grids.large =>
        grid = new Grid(16, 17)
    }
    println("create")
    println(grid.rows)
    //println(grid)
  }


  def setValueToBottom(column: Int, value: Int): Try[Int] = {
    this.setValueToGrid(grid.col(column), grid.cells.row - 1, column, value)
  }

  def checkWinner(row: Int, col: Int, stone: Int): Boolean = {
    grid.is4Stone(row, col, stone)
  }

  def setValueToGrid(cells: Field, row: Int, col: Int, stone: Int): Try[Int] = {
    Try(cells.cell(row).equals(Cell(0))) match {
      case Success(cond) => {
        if (cond) {
          undoManager.doStep(new SetCommand(row, col, stone, this))
          Success(row)
        } else {
          setValueToGrid(cells, row - 1, col, stone)
        }
      }
      case Failure(exception) => Failure(exception)
    }
  }

  def undo: Unit = {
    undoManager.undoStep
  }

  def redo: Unit = {
    undoManager.redoStep
  }

  def save(rows:Int,cols:Int,grid:String):Unit = db.saveGrid(rows,cols,grid)

  def load() = {
    val result = db.loadLastGrid
    val a_grid = result._4
    val rows:Array[Array[String]] = a_grid.split("\r\n").map(_.trim).toArray.map(row => row.stripPrefix(" ").split(" ").map(_.trim).toArray)
    grid = new Grid(result._2,result._3)
    for (row <- 0 to result._2 - 1;
         col <- 0 to result._3 - 1) {
      grid = grid.set(row,col, rows(row)(col).toInt)
    }
    //println(grid)
  }

  def gridToJson: JsObject = Json.obj(
    "size" -> JsNumber(this.grid.rows * this.grid.cols),
    "rows" -> JsNumber(this.grid.rows),
    "cols" -> JsNumber(this.grid.cols),
    "cells" -> Json.toJson(
      for {
        row <- 0 until this.grid.rows
        col <- 0 until this.grid.cols
      } yield {
        Json.obj(
          "row" -> row,
          "col" -> col,
          "cell" -> Json.toJson(this.grid.cell(row, col))
        )
      }
    )
  )
}
