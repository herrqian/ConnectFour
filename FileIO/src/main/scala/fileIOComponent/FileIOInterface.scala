package main.scala.fileIOComponent

import PlayerModul.Player
import main.scala.model.gridComponent.GridInterface

import scala.util.Try

trait FileIOInterface {

    def load:Try[(Option[GridInterface],Array[Player])]
    def save(grid: GridInterface, players:Array[Player]): Try[Unit]

}
