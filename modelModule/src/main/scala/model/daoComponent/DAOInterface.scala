package model.daoComponent

import main.scala.model.gridComponent.GridInterface

trait DAOInterface {

  def saveGrid(rows:Int,cols:Int,grid:String): Unit

  def loadLastGrid(): (Int, Int, Int, String)
}
