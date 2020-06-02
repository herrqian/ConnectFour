package controller

import main.scala.model.gridComponent.gridBaseImpl.Grid
import org.scalatest.{Matchers, WordSpec}
import player.Player
class ControllerSpec extends WordSpec with Matchers {
  "a controller" when {
    "observed by an Observer" should {
      val aGrid = new Grid(6, 7)
      val controller = new GridController(aGrid)

      "test the createEmptyGrid function" in {
        controller.createEmtpyField("Grid Small")
        controller.grid should be(new Grid(6,7))
        controller.createEmtpyField("Grid Middle")
        controller.grid should be(new Grid(10,11))
        controller.createEmtpyField("Grid Large")
        controller.grid should be(new Grid(16,17))
      }
      "test the setValueToBottom function" in {
        controller.setValueToBottom(0,1)
        controller.grid.col(0).cell(15).value should be(1)
      }
      "test the undo and redo functions" in {
        controller.undo
        controller.grid.col(0).cell(15).value should be(0)
        controller.redo
        controller.grid.col(0).cell(15).value should be(1)
      }
    }
  }
}
