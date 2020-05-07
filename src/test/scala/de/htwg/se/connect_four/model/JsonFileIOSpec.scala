package de.htwg.se.connect_four.model

import java.io.File

import de.htwg.se.connect_four.model.fileIOComponent.fileIoJsonImpl.FileIO
import de.htwg.se.connect_four.model.gridComponent.gridAdvancedImpl.Grid
import org.scalatest.{Matchers, WordSpec}

class JsonFileIOSpec extends WordSpec with Matchers {
  "A FileIO is used to handle input and output from json-file" when {
    val jsonio = new FileIO()
    val grid = new Grid(6,7)
    val mgrid = new Grid(10,11)
    val lgrid = new Grid(16,17)
    val playerarray = Array(true,false)
    "tests for small grid" in {
      jsonio.save(grid, playerarray)
      new File("grid.json").exists() should be(true)
      val (loadgrid, loadplayers) = jsonio.load
      loadgrid should be(Some(grid))
      loadplayers should be(playerarray)
      new File("grid.json").delete() should be(true)
    }
    "tests for large grid" in {
      jsonio.save(lgrid, playerarray)
      new File("grid.json").exists() should be(true)
      val (loadgrid, loadplayers) = jsonio.load
      loadgrid should be(Some(lgrid))
      loadplayers should be(playerarray)
      new File("grid.json").delete() should be(true)
    }
    "tests for middle grid" in {
      jsonio.save(mgrid, playerarray)
      new File("grid.json").exists() should be(true)
      val (loadgrid, loadplayers) = jsonio.load
      loadgrid should be(Some(mgrid))
      loadplayers should be(playerarray)
      new File("grid.json").delete() should be(true)
    }
  }

}
