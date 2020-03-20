package de.htwg.se.connect_four.model

import de.htwg.se.connect_four.model.gridComponent.gridBaseImpl.{Cell, Grid, Matrix}
import org.scalatest.{Matchers, WordSpec}

class GridSpec extends WordSpec with Matchers {
  "A Grid is the playingfield of Connect Four. A Grid" when {
    val smallGrid = Grid(new Matrix[Cell](Vector(Vector(Cell(1), Cell(2), Cell(3)), Vector(Cell(4), Cell(5), Cell(6)), Vector(Cell(7), Cell(8), Cell(9)))))
    val grid = new Grid(2,3)
    val tinyGrid = Grid(new Matrix[Cell](Vector(Vector(Cell(1)))))
    val aGrid = new Grid(4, 5)
    "toString function tested" in {
      val a_string = "0 0 0 " + System.lineSeparator() + "0 0 0 " + System.lineSeparator()
      grid.toString should be(a_string)
    }
    "give access to its Cells" in {
      aGrid.cell(0, 0) should be(Cell(0))
      aGrid.cell(0, 1) should be(Cell(0))
      aGrid.cell(1, 0) should be(Cell(0))
      aGrid.cell(1, 1) should be(Cell(0))
    }
    "allow to set individual Cells and remain immutable" in {
      val changedGrid = aGrid.set(0, 0, 1)
      changedGrid.cell(0, 0) should be(Cell(1))
      aGrid.cell(0, 0) should be(Cell(0))
    }
    "have the right values in the right places" in {
      smallGrid.cell(0, 0) should be(Cell(1))
      smallGrid.cell(0, 1) should be(Cell(2))
      smallGrid.cell(1, 0) should be(Cell(4))
      smallGrid.cell(1, 1) should be(Cell(5))
    }
    "have Field with the right Cells" in {
      tinyGrid.row(0).cell(0) should be(Cell(1))
      tinyGrid.col(0).cell(0) should be(Cell(1))

      smallGrid.row(0).cell(0) should be(Cell(1))
      smallGrid.row(0).cell(1) should be(Cell(2))
      smallGrid.row(1).cell(0) should be(Cell(4))
      smallGrid.row(1).cell(1) should be(Cell(5))
      smallGrid.col(0).cell(0) should be(Cell(1))
      smallGrid.col(0).cell(1) should be(Cell(4))
      smallGrid.col(1).cell(0) should be(Cell(2))
      smallGrid.col(1).cell(1) should be(Cell(5))
      smallGrid.col(2).getCells should be(Vector(Cell(3), Cell(6), Cell(9)))
    }
    "have Field with the diagonal" in {
      smallGrid.link_diagonal(1,0).cell(0) should be(Cell(4))
      smallGrid.link_diagonal(1, 0).cell(1) should be(Cell(2))
      smallGrid.right_diagonal(1,0).cell(0) should be(Cell(4))
      smallGrid.right_diagonal(1,0).cell(1) should be(Cell(8))
      smallGrid.right_diagonal(1,1).cell(0) should be(Cell(1))
      smallGrid.right_diagonal(1,1).cell(1) should be(Cell(5))
      smallGrid.link_diagonal(1,1).getCells should be(Vector(Cell(7),Cell(5),Cell(3)))
      smallGrid.right_diagonal(1,1).getCells should be(Vector(Cell(1), Cell(5), Cell(9)))
      smallGrid.link_diagonal(1,0).getCells should be(Vector(Cell(4), Cell(2)))
      smallGrid.right_diagonal(1,0).getCells should be(Vector(Cell(4), Cell(8)))
    }
  }
}
