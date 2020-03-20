package de.htwg.se.connect_four.controller
import de.htwg.se.connect_four.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.connect_four.model.gridComponent.gridBaseImpl.Grid
import de.htwg.se.connect_four.util.Observer
import org.scalatest.{Matchers, WordSpec}
class ControllerSpec extends WordSpec with Matchers {
  "a controller" when {
    "observed by an Observer" should {
      val aGrid = new Grid(2, 3)
      val controller = new Controller(aGrid)
      val observer = new Observer() {
        var updated: Boolean = false

        def isUpdated: Boolean = updated

        override def update(): Boolean = {
          updated = true
          true
        }
      }
      "test the checkWinner function" in {
        controller.checkWinner(0,0) should be(false)
        controller.checkWinner(1,0) should be(false)
      }
      "test the getTrun function" in {
        controller.getTurn(0) should be(true)
      }
      "test the changeTurn function" in {
        controller.changeTurn()
        controller.getTurn(0) should be(false)
      }
      "test the currentPlayer function" in {
        controller.currentPlayer() should be(2)
      }
    }
  }
}
