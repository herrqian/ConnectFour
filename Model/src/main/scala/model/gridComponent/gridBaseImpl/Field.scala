package main.scala.model.gridComponent.gridBaseImpl

case class Field(cells: Vector[Cell]) {
  def cell(index: Int): Cell = cells(index)
}
