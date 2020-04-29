package de.htwg.se.connect_four.model

import java.io.File

import de.htwg.se.connect_four.model.fileIOComponent.fileIoXmlImpl.FileIO
import de.htwg.se.connect_four.model.gridComponent.gridAdvancedImpl.Grid
import org.scalatest.{Matchers, WordSpec}

class XmlFileIOSpec extends WordSpec with Matchers {
  "A FileIO is used to handle input and output from json-file" when {
    val xmlio = new FileIO()
    val grid = new Grid(6,7)
    val mgrid = new Grid(10,11)
    val lgrid = new Grid(16,17)
    val playerarray = Array(true,false)
    "tests for small grid" in {
      xmlio.save(grid, playerarray)
      new File("grid.xml").exists() should be(true)
      val (loadgrid, loadplayers) = xmlio.load
      loadgrid should be(Some(grid))
      loadplayers should be(playerarray)
      new File("grid.xml").delete() should be(true)
    }
    "tests for large grid" in {
      xmlio.save(lgrid, playerarray)
      new File("grid.xml").exists() should be(true)
      val (loadgrid, loadplayers) = xmlio.load
      loadgrid should be(Some(lgrid))
      loadplayers should be(playerarray)
      new File("grid.xml").delete() should be(true)
    }
    "tests for middle grid" in {
      xmlio.save(mgrid, playerarray)
      new File("grid.xml").exists() should be(true)
      val (loadgrid, loadplayers) = xmlio.load
      loadgrid should be(Some(mgrid))
      loadplayers should be(playerarray)
      new File("grid.xml").delete() should be(true)
    }
  }

}
