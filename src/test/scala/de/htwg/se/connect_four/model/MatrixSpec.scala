package de.htwg.se.connect_four.model

import de.htwg.se.connect_four.model.gridComponent.gridBaseImpl.{Cell, Matrix}
import org.scalatest.{Matchers, WordSpec}

class MatrixSpec extends WordSpec with Matchers {
  "A Matrix is a tailor-made immutable data type that contains a two-dimentional Vector of Cells. A Matrix" when {
    "empty" should {
      "be created by 4x5 matrix filled with empty cells" in {
        val emtpyMatrix = new Matrix[Cell](4, 5, Cell(0))
        emtpyMatrix.row should be(4)
        emtpyMatrix.col should be(5)
        emtpyMatrix.size should be(20)
      }
      "for test purposes only be created with a Vector of Vectors" in {
        val testMatrix = Matrix(Vector(Vector(Cell(0))))
        testMatrix.size should be(1)
      }
    }

    "filled" should {
      val matrix = Matrix(Vector(Vector(Cell(0),Cell(1)), Vector(Cell(0), Cell(2))))
      "give a access to its cells" in {
        matrix.cell(0,0) should be(Cell(0))
        matrix.cell(0,1) should be(Cell(1))
        matrix.cell(1,0) should be(Cell(0))
        matrix.cell(1,1) should be(Cell(2))
      }
      "replace cells and return a new data structure" in {
        val returnedMatrix = matrix.replaceCell(0,0,Cell(1))
        matrix.cell(0,0) should be(Cell(0))
        returnedMatrix.cell(0,0) should be(Cell(1))
      }
      "be filled using fill operation" in {
        val returnedMatrix = matrix.fill(Cell(2))
        returnedMatrix.cell(0,0) should be(Cell(2))
        returnedMatrix.cell(0,1) should be(Cell(2))
      }
    }
  }

}
