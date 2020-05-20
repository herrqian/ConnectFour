package main.scala.model.gridComponent.gridBaseImpl

import main.scala.model.gridComponent.CellInterface
import play.api.libs.json.Json

case class Cell(value: Int) extends CellInterface {

  def isSet: Boolean = {
    value != 0
  }

  override def toString: String = value.toString
}

object Cell {

  implicit val cellWrites = Json.writes[Cell]
  implicit val cellReads = Json.reads[Cell]
}