package controller

import main.scala.model.gridComponent.GridInterface
import main.scala.model.gridComponent.gridAdvancedImpl.Grid
import play.api.libs.json.{JsNumber, JsObject, Json}

class GridController(var grid :GridInterface) {

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
