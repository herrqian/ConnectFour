package controller

import main.scala.model.gridComponent.GridInterface
import main.scala.model.gridComponent.gridAdvancedImpl.Grid
import main.scala.model.gridComponent.gridBaseImpl.{Cell, Field}
import play.api.libs.json.{JsNumber, JsObject, Json}
import util.UndoManager

import scala.util.{Failure, Success, Try}

class GridController(var grid :GridInterface) {

  private val undoManager = new UndoManager

  object Grids extends Enumeration {
    type Grids = Value
    val small = Value("Grid Small").toString
    val middle = Value("Grid Middle").toString
    val large = Value("Grid Large").toString
  }

  def createEmtpyField(size:String): Unit = {
    size match {
      case Grids.small =>
        grid = new Grid(6,7)
      case Grids.middle =>
        grid =new Grid(10,11)
      case Grids.large =>
        grid = new Grid(16,17)
    }
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

  def undo:Unit = {
    undoManager.undoStep
  }

  def redo:Unit = {
    undoManager.redoStep
  }

  def gridToJson: JsObject = Json.obj(
    "size" -> JsNumber(grid.rows * grid.cols),
    "rows" -> JsNumber(grid.rows),
    "cols" -> JsNumber(grid.cols),
    "cells" -> Json.toJson(
      for {
        row <- 0 until grid.rows
        col <- 0 until grid.cols
      } yield {
        Json.obj(
          "row" -> row,
          "col" -> col,
          "cell" -> Json.toJson(grid.cell(row, col))
        )
      }
    )
  )
}
