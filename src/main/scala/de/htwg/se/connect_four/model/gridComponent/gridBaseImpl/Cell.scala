package de.htwg.se.connect_four.model.gridComponent.gridBaseImpl

import de.htwg.se.connect_four.model.gridComponent.CellInterface

case class Cell(value: Int) extends CellInterface {

  def isSet: Boolean = {
    value != 0
  }
  def set(value: Int): Cell = Cell(value)
  override def toString: String = value.toString
}

object Cell {
  import play.api.libs.json._
  implicit val cellWrites = Json.writes[Cell]
  implicit val cellReads = Json.reads[Cell]
}