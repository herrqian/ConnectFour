package controller

import util.Command


class SetCommand(row: Int, col:Int, value:Int, controller: GridController) extends  Command {
  override def doStep: Unit = {
    controller.grid = controller.grid.set(row,col,value)
  }

  override def undoStep: Unit = {
    controller.grid = controller.grid.set(row, col, 0)
  }

  override def redoStep: Unit = {
    controller.grid = controller.grid.set(row, col, value)
  }

}
