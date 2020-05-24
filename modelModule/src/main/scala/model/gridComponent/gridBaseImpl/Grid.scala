package main.scala.model.gridComponent.gridBaseImpl

import main.scala.model.gridComponent.GridInterface
import play.api.libs.json.Json

import scala.collection.mutable.ArrayBuffer

case class Grid(cells: Matrix[Cell]) extends GridInterface {
  def this(row: Int, col: Int) = this(new Matrix[Cell](row, col, Cell(0)))

  def rows: Int = cells.row

  def cols: Int = cells.col

  def cell(row: Int, col: Int): Cell = cells.cell(row, col)

  def set(row: Int, col: Int, value: Int): Grid = copy(cells.replaceCell(row, col, Cell(value)))

  def row(row: Int): Field = Field(cells.rows(row))

  def col(col: Int): Field = Field(cells.rows.map(row => row(col)))

  def link_diagonal(row: Int, col: Int): Field = {
    val array = left_diagonal_recOne(row, col)
    val mvec = ArrayBuffer[Cell]()
    left_diagonal_recTwo(array(0), array(1), mvec)
    Field(mvec.toVector)
  }


  def left_diagonal_recOne(row: Int, col: Int): Array[Int] = {
    if (row < cells.row - 1 && col > 0) {
      left_diagonal_recOne(row + 1, col - 1)
    } else {
      Array(row, col)
    }
  }

  def left_diagonal_recTwo(row: Int, col: Int, mvec: ArrayBuffer[Cell]): Unit = {
    if (row >= 0 && col < cells.col) {
      mvec.append(cells.cell(row, col))
      left_diagonal_recTwo(row - 1, col + 1, mvec)
    }
  }


  def right_diagonal(row: Int, col: Int): Field = {
    val array = right_diagonal_recOne(row, col)
    val mvec = ArrayBuffer[Cell]()
    right_diagonal_recTwo(array(0), array(1), mvec)
    Field(mvec.toVector)
  }

  def right_diagonal_recOne(row: Int, col: Int): Array[Int] = {
    if (row > 0 && col > 0) {
      right_diagonal_recOne(row = row - 1, col = col - 1)
    } else {
      Array(row, col)
    }
  }

  def right_diagonal_recTwo(row: Int, col: Int, mvec: ArrayBuffer[Cell]): Unit = {
    if (row < cells.row && col < cells.col) {
      mvec.append(cells.cell(row, col))
      right_diagonal_recTwo(row = row + 1, col = col + 1, mvec)
    }
  }

  def is4Stone(row: Int, col: Int, n: Int): Boolean = {
    if (this.check4number(this.row(row).cells, n, 0)
      || this.check4number(this.col(col).cells, n, 0)
      || this.check4number(this.link_diagonal(row, col).cells, n, 0)
      || this.check4number(this.right_diagonal(row, col).cells, n, 0)) {
      true
    } else {
      false
    }
  }

  private def check4number(vec: Vector[Cell], n: Int, counter: Int): Boolean = {
    if (counter == 4)
      return true
    vec.headOption match {
      case Some(head) => {
        if (head.equals(Cell(n))) {
          check4number(vec.tail, n, counter + 1)
        } else {
          check4number(vec.tail, n, 0)
        }
      }
      case None => counter == 4
    }
  }

  override def toString: String = cells.toString

  override def toHTML: String = "<p  style=\"font-family:'Lucida Console', monospace\"> " + toString.replace("\n","<br>").replace("  "," _") +"</p>"
}

object Grid {

  implicit val gridWrites = Json.writes[Grid]
  implicit val gridReads = Json.reads[Grid]
}