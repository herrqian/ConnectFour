package model

import java.io.File

import main.scala.fileIOComponent.fileIoJsonImpl.FileIO
import main.scala.model.gridComponent.gridAdvancedImpl.Grid
import org.scalatest.{Matchers, WordSpec}
import player.Player

import scala.util.{Failure, Success}

class JsonFileIOSpec extends WordSpec with Matchers {
  "A FileIO is used to handle input and output from json-file" when {
    val jsonio = new FileIO()
    val grid = new Grid(6,7)
    val mgrid = new Grid(10,11)
    val lgrid = new Grid(16,17)
    val playerarray:Array[Player] = Array(Player("Player1"),Player("Player2"))
    "tests for small grid" in {
      jsonio.save(grid, playerarray)
      new File("grid.json").exists() should be(true)
      jsonio.load match {
        case Success(value) => {
          value._1 should be(Some(grid))
          //value._2 should be(playerarray)
        }
        case Failure(exception) => println(exception.toString)
      }
      new File("grid.json").delete() should be(true)
    }
    "tests for large grid" in {
      jsonio.save(lgrid, playerarray)
      new File("grid.json").exists() should be(true)
      jsonio.load match {
        case Success(value) => {
          value._1 should be(Some(lgrid))
          //value._2 should be(playerarray)
        }
        case Failure(exception) => println(exception.toString)
      }
      new File("grid.json").delete() should be(true)
    }
    "tests for middle grid" in {
      jsonio.save(mgrid, playerarray)
      new File("grid.json").exists() should be(true)
      jsonio.load match {
        case Success(value) => {
          value._1 should be(Some(mgrid))
          //value._2 should be(playerarray)
        }
        case Failure(exception) => println(exception.toString)
      }
      new File("grid.json").delete() should be(true)
    }
  }

}
