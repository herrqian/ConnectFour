package de.htwg.se.connect_four.model.fileIOComponent
import de.htwg.se.connect_four.model.gridComponent.GridInterface
import play.api.libs.json.JsValue
trait FileIOInterface {

    def load:(Option[GridInterface],Array[Boolean])
    def save(grid: GridInterface, players:Array[Boolean]): Unit

}
