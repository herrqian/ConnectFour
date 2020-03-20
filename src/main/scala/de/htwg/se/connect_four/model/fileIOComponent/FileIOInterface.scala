package de.htwg.se.connect_four.model.fileIOComponent
import de.htwg.se.connect_four.model.gridComponent.GridInterface
trait FileIOInterface {

    def load: (GridInterface,Array[Boolean])
    def save(grid: GridInterface, players:Array[Boolean]): Unit

}
