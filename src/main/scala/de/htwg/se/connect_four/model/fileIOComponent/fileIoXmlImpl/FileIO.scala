package de.htwg.se.connect_four.model.fileIOComponent.fileIoXmlImpl

import com.google.inject.Guice
import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions._
import de.htwg.se.connect_four.ConnectFourModule
import de.htwg.se.connect_four.model.fileIOComponent.FileIOInterface
import de.htwg.se.connect_four.model.gridComponent.GridInterface
import java.io._
import scala.xml.{Elem, Node, PrettyPrinter}
import scala.util.{Failure, Success, Try}

class FileIO extends FileIOInterface {


  override def load: (Option[GridInterface], Array[Boolean]) = {
    val file = scala.xml.XML.loadFile("grid.xml")
    val sizeAttr = (file \\ "grid" \ "@size")
    val player1 = (file \\ "grid" \ "@player1").text.toBoolean
    val player2 = (file \\ "grid" \ "@player2").text.toBoolean
    val cells_map = file \\ "cell" map { col => ((col \ "@col").text + (col \ "@row").text  -> col.text.trim) } toMap
    val cells_list = cells_map.toList
    val size = sizeAttr.text.toInt
    val injector = Guice.createInjector(new ConnectFourModule)
    val cellNodes = (file \\ "cell")

    val grid:Option[GridInterface] =  size match {
      case 42 => set_grid(cells_list, Some(injector.instance[GridInterface](Names.named("Grid Small"))), 0)
      case 110 => set_grid(cells_list, Some(injector.instance[GridInterface](Names.named("Grid Middle"))), 0)
      case 272 => set_grid(cells_list, Some(injector.instance[GridInterface](Names.named("Grid Large"))), 0)
    }

    /*

    for (cell <- cellNodes) {
      val row: Int = (cell \ "@row").text.toInt
      val col: Int = (cell \ "@col").text.toInt
      val value: Int = cell.text.trim.toInt
      grid = grid match {
        case Some(g) => Some(g.set(row, col, value))
        case None => None
      }
    }

    */
    (grid, Array(player1,player2))

  }



  def set_grid(cells_list: List[(String,String)], grid: Option[GridInterface], index: Int): Option[GridInterface]  = {
    if (index < cells_list.size) {

      val col: Int = cells_list.apply(index)._1.substring(0,1).toInt
      val row: Int = cells_list.apply(index)._1.substring(1,2).toInt
      val value: Int = cells_list.apply(index)._2.toInt
      Try(grid) match {

        case Success(g) => set_grid(cells_list, Some(g.get.set(row, col, value)), index +1)
        case Failure(_)  => None
      }

    } else {
      grid
    }
  }

  def linearize(node: Node): List[Node] = {

    node :: node.child.flatMap {
      case e: Elem => linearize(e)
      case _ => Nil
    }.toList

  }

  override def save(grid: GridInterface, players: Array[Boolean]): Unit = {saveString(grid, players)}

  def saveString(interface: GridInterface, players: Array[Boolean]) = {
    val pw = new PrintWriter(new File("grid.xml"))
    val prettyPrinter = new PrettyPrinter(120,4)
    val xml = prettyPrinter.format(toXml(interface, players))
    pw.write(xml)
    pw.close()
  }

  def toXml(interface: GridInterface, players: Array[Boolean]) = {
    <grid size={(interface.rows * interface.cols).toString} player1={players.apply(0).toString} player2={players.apply(1).toString}>
      {
      val rows = interface.rows
      val cols = interface.cols
      for {
        row <- 0 until rows
        col <- 0 until cols
      } yield cellToXml(interface, row, col)}

    </grid>
  }

  def cellToXml(grid: GridInterface, row: Int, col: Int) = {
    <cell row={ row.toString } col={ col.toString }>
      { grid.cell(row, col).value }
    </cell>
  }

}
