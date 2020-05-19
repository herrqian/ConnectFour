package main.scala.fileIOComponent.fileIoXmlImpl

import java.io._

import PlayerModul.{Player, PlayerInterface}
import com.google.inject.Guice
import com.google.inject.name.Names
import main.scala.fileIOComponent.FileIOInterface
import main.scala.model.gridComponent.GridInterface
import main.scala.model.gridComponent.gridBaseImpl.Grid
import scala.util.{Failure, Success, Try}
import scala.xml.{Elem, Node, PrettyPrinter}

class FileIO extends FileIOInterface {

  final val FILE_NAME = "grid.xml"

  override def load: Try[(Option[GridInterface], Array[Player])] = {
    Try {
      val file = scala.xml.XML.loadFile(FILE_NAME)
      val sizeAttr = (file \\ "grid" \ "@size")
      val player1 = Player((file \\ "grid" \ "@player1").text)
      val player2 = Player((file \\ "grid" \ "@player2").text)
      val cells_map = file \\ "cell" map { col => ((col \ "@col").text + (col \ "@row").text -> col.text.trim) } toMap
      val cells_list = cells_map.toList
      val size = sizeAttr.text.toInt

      val grid: Option[GridInterface] = size match {
        case 42 => set_grid(cells_list, Some(new Grid(6,7)), 0)
        case 110 => set_grid(cells_list, Some(new Grid(10,11)), 0)
        case 272 => set_grid(cells_list, Some(new Grid(16,17)), 0)
      }

      (grid, Array(player1, player2))
    }

  }


  def set_grid(cells_list: List[(String, String)], grid: Option[GridInterface], index: Int): Option[GridInterface] = {
    if (index < cells_list.size) {

      val col: Int = cells_list.apply(index)._1.substring(0, 1).toInt
      val row: Int = cells_list.apply(index)._1.substring(1, 2).toInt
      val value: Int = cells_list.apply(index)._2.toInt
      Try(grid) match {

        case Success(g) => set_grid(cells_list, Some(g.get.set(row, col, value)), index + 1)
        case Failure(_) => None
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

  override def save(grid: GridInterface, players: Array[Player]): Try[Unit] = {
    Try {
      saveString(grid, players)
    }
  }

  def saveString(interface: GridInterface, players: Array[Player]) = {
    val pw = new PrintWriter(new File(FILE_NAME))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(toXml(interface, players))
    pw.write(xml)
    pw.close()
  }

  def toXml(interface: GridInterface, players: Array[Player]) = {
    <grid size={(interface.rows * interface.cols).toString} player1={players.apply(0).toString} player2={players.apply(1).toString}>
      {val rows = interface.rows
    val cols = interface.cols
    for {
      row <- 0 until rows
      col <- 0 until cols
    } yield cellToXml(interface, row, col)}

    </grid>
  }

  def cellToXml(grid: GridInterface, row: Int, col: Int) = {
    <cell row={row.toString} col={col.toString}>
      {grid.cell(row, col).value}
    </cell>
  }

}
