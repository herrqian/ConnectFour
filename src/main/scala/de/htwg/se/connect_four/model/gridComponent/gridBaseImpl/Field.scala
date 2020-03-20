package de.htwg.se.connect_four.model.gridComponent.gridBaseImpl

case class Field(cells:Vector[Cell]) {
  def cell(index:Int):Cell=cells(index)
  def getCells: Vector[Cell] = cells
}
