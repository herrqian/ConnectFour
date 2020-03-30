package de.htwg.se.connect_four.model.gridComponent.gridBaseImpl

import de.htwg.se.connect_four.model.gridComponent.GridInterface

import scala.collection.mutable.ArrayBuffer

case class Grid(cells: Matrix[Cell]) extends GridInterface {
  def this(row: Int, col:Int) = this(new Matrix[Cell](row, col, Cell(0)))
  def rows: Int = cells.row
  def cols:Int = cells.col
  def cell(row:Int, col: Int): Cell=cells.cell(row,col)
  def set(row:Int, col: Int, value:Int): Grid=copy(cells.replaceCell(row,col,Cell(value)))
  def row(row: Int):Field=Field(cells.rows(row))
  def col(col:Int):Field=Field(cells.rows.map(row=>row(col)))

  def link_diagonal(row:Int, col:Int):Field = {
    val mrow = row
    val mcol = col
    val mvec = ArrayBuffer[Cell]()

/*
    while (mrow < cells.row - 1 && mcol > 0) {
      mrow = mrow + 1
      mcol = mcol - 1
    }
    while (mrow >= 0 && mcol < cells.col) {
      mvec.append(cells.cell(mrow, mcol))
      mrow = mrow - 1
      mcol = mcol + 1
    }
*/
    //while in recursive function
    left_diagonal_recOne(mrow,mcol)
    left_diagonal_recTwo(mrow,mcol,mvec)
    Field(mvec.toVector)
  }

  def left_diagonal_recOne(row: Int, col: Int): Unit = {
    if(row < cells.row - 1 && row > 0) left_diagonal_recOne(row+1,col-1)
  }

  def left_diagonal_recTwo(row: Int, col: Int, mvec: ArrayBuffer[Cell]): Unit = {
    if(row >= 0 && col < cells.col){
      mvec.append(cells.cell(row, col))
      left_diagonal_recTwo(row-1,col+1, mvec)
    }
  }


  def right_diagonal(row: Int, col: Int):Field = {
    val mrow = row
    val mcol = col
    val mvec = ArrayBuffer[Cell]()
/*
     while (mrow > 0 && mcol > 0) {
      mrow = mrow - 1
      mcol = mcol - 1
    }

     while (mrow < cells.row && mcol < cells.col) {
      mvec.append(cells.cell(mrow, mcol))
      mrow = mrow + 1
      mcol = mcol + 1
    }
*/
    //while in recursive function
    right_diagonal_recOne(mrow,mcol)
    right_diagonal_recTwo(mrow,mcol,mvec)
    Field(mvec.toVector)
  }

  def right_diagonal_recOne(row: Int, col: Int): Unit = {
    if(row > 1 && col > 0) right_diagonal_recOne(row-1,col-1)
  }

  def right_diagonal_recTwo(row: Int, col: Int, mvec: ArrayBuffer[Cell]): Unit = {
    if(row < cells.row && col < cells.col){
      mvec.append(cells.cell(row, col))
      right_diagonal_recTwo(row+1,col+1, mvec)
    }
  }

  override def toString: String = cells.toString
}

object Grid {
  import play.api.libs.json._
  implicit val gridWrites = Json.writes[Grid]
  implicit val gridReads = Json.reads[Grid]
}