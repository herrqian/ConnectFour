package main.scala.aview

import main.scala.controller.controllerBaseImpl.Controller
import main.scala.model.gridComponent.gridBaseImpl.Grid
import org.scalatest.{Matchers, WordSpec}

class TuiSpec extends WordSpec with Matchers {
  "a Tui" when {
    "his function processInputLine is to test" should {
      val controller = new Controller(new Grid(6,7))
      val tui = new Tui(controller)
      "input n will create a new grid" in {
        tui.processInputLine("n small")
        controller.grid should be(new Grid(6,7))
        tui.processInputLine("n middle")
        controller.grid should be(new Grid(10,11))
        tui.processInputLine("n large")
        controller.grid should be(new Grid(16,17))
      }
      "input i1 will insert a value to column 1" in {
        tui.processInputLine("i1")
        val a_grid = new Grid(16, 17).set(15, 1, 1)
        controller.grid should be(a_grid)
      }
      "input q will do nothing" in {
        tui.processInputLine("q")
      }
      "any non-configured input will do nothing" in {
        tui.processInputLine("")
      }
      "input out of bounds will do nothing" in {
        tui.processInputLine("i9")
      }
      "if player has won winnerCheck will be true" in {
        tui.processInputLine("i1")
        tui.processInputLine("i2")
        tui.processInputLine("i1")
        tui.processInputLine("i2")
        tui.processInputLine("i1")
        tui.processInputLine("i2")
        tui.processInputLine("i1")
        tui.winnerCheck should be(true)
      }
    }
  }
}