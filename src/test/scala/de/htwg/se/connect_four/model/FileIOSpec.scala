package de.htwg.se.connect_four.model

import java.io.File

import de.htwg.se.connect_four.model.fileIOComponent.fileIoJsonImpl.FileIO
import de.htwg.se.connect_four.model.gridComponent.gridAdvancedImpl.Grid
import org.scalatest.{Matchers, WordSpec}

import scala.Some

class FileIOSpec extends WordSpec with Matchers {
  "A FileIO is used to handle input and output from json-file" when {
    val jsonio = new FileIO()
    val grid = new Grid(6,7)
    val mgrid = new Grid(10,11)
    val lgrid = new Grid(16,17)
    val playerarray = Array(true,false)
    "it has a save function" in {
      jsonio.save(lgrid, playerarray)
      new File("grid.json").exists() should be(true)
      jsonio.save(mgrid, playerarray)
      new File("grid.json").exists() should be(true)
      jsonio.save(grid, playerarray)
      new File("grid.json").exists() should be(true)
    }
    "it has a load function" in {
      val (loadgrid, loadplayers) = jsonio.load
      loadgrid should be(Some(grid))
      loadplayers should be(playerarray)
      new File("grid.json").delete() should be(true)
    }
  }

}
