package de.htwg.se.connect_four.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.connect_four.util.Command

class SetCommand(row: Int, col:Int, value:Int, controller: Controller) extends  Command {
  override def doStep: Unit = {
    controller.grid = controller.grid.set(row,col,value)
  }

  override def undoStep: Unit = {
    controller.grid = controller.grid.set(row, col, 0)
    controller.changeTurn()
  }

  override def redoStep: Unit = {
    controller.grid = controller.grid.set(row, col, value)
    controller.changeTurn()
  }

}
