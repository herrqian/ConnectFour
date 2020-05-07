package de.htwg.se.connect_four.model.fileIOComponent
import de.htwg.se.connect_four.model.gridComponent.GridInterface
import play.api.libs.json.JsValue

import scala.util.Try
trait FileIOInterface {

    def load:Try[(Option[GridInterface],Array[Boolean])]
    def save(grid: GridInterface, players:Array[Boolean]): Try[Unit]

}
