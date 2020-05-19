package de.htwg.se.connect_four.controller.controllerComponent.controllerBaseImpl

import com.google.inject.name.Names
import com.google.inject.{Guice, Inject}
import net.codingwell.scalaguice.InjectorExtensions._
import de.htwg.se.connect_four.ConnectFourModule
import de.htwg.se.connect_four.controller.controllerComponent.GameStatus.{IDLE, WIN}
import de.htwg.se.connect_four.model.gridComponent.GridInterface
import de.htwg.se.connect_four.model.gridComponent.gridBaseImpl.{Cell, Field, Matrix}
import de.htwg.se.connect_four.util.UndoManager
import de.htwg.se.connect_four.controller.controllerComponent.{CellChanged, ControllerInterface, GameStatus, GridChanged, GridSizeChanged, LoadError, SaveError, SetError, WinEvent}
import de.htwg.se.connect_four.model.fileIOComponent.FileIOInterface

import scala.util.{Failure, Success, Try}

class Controller @Inject()(var grid: GridInterface) extends ControllerInterface {

  var playerList = Array(true, false)
  var gameStatus = IDLE
  private val undoManager = new UndoManager
  val injector = Guice.createInjector(new ConnectFourModule)
  val fileIo = injector.instance[FileIOInterface]
  val rowsCols = Map("gridrow" -> 6, "gridcol" -> 7)

  def save(): Unit = {
    fileIo.save(grid, playerList) match {
      case Success(_) => publish(new GridChanged)
      case Failure(exception) => publish(new SaveError(exception.toString))
    }
  }

  def load(): Unit = {
    fileIo.load match {
      case Success(data) => {
        grid = data._1 match {
          case Some(g) => g
        }
        playerList = data._2
        publish(new GridSizeChanged("new size"))
        publish(new GridChanged)
      }
      case Failure(exception) => publish(new LoadError(exception.toString))
    }
  }

  object Grids extends Enumeration {
    type Grids = Value
    val small = Value("Grid Small").toString
    val middle = Value("Grid Middle").toString
    val large = Value("Grid Large").toString
  }


  def createEmptyGrid(s: String): Unit = {
    s match {
      case Grids.small =>
        grid = injector.instance[GridInterface](Names.named(Grids.small))
      case Grids.middle =>
        grid = injector.instance[GridInterface](Names.named(Grids.middle))
      case Grids.large =>
        grid = injector.instance[GridInterface](Names.named((Grids.large)))
    }
    resetPlayerList()
    gameStatus = IDLE
    publish(new GridSizeChanged(s))
  }


  def setValueToBottom(column: Int): Unit = {
    val value = if (playerList(0)) {
      1
    } else {
      2
    }
    this.setValueR(grid.col(column), grid.cells.row - 1, column, value) match {
      case Success(row) => {
        if (this.checkWinner(row, column, value)) {
          gameStatus = WIN
          publish(new WinEvent(value))
        } else {
          this.changeTurn()
          publish(new CellChanged(row, column, value))
        }
      }
      case Failure(exception) => publish(new SetError(exception.toString))
    }

  }

  private def setValueR(cells: Field, row: Int, col: Int, stone: Int): Try[Int] = {
    Try(cells.cell(row).equals(Cell(0))) match {
      case Success(cond) => {
        if (cond) {
          undoManager.doStep(new SetCommand(row, col, stone, this))
          Success(row)
        } else {
          setValueR(cells, row - 1, col, stone)
        }
      }
      case Failure(exception) => Failure(exception)
    }
  }

  def checkWinner(row: Int, col: Int, stone: Int): Boolean = {
    grid.is4Stone(row, col, stone)
  }

  def getTurn(playerNumber: Int): Boolean = {
    playerList(playerNumber)
  }

  def changeTurn(): Unit = {
    playerList(0) = !playerList(0)
    playerList(1) = !playerList(1)
  }

  def currentPlayer(): Int = {
    if (playerList(0)) {
      return 1
    }
    2
  }

  def resetPlayerList(): Unit = {
    playerList = Array(true, false)
  }

  def gridToString: String = grid.toString

  def undo: Unit = {
    undoManager.undoStep
    publish(new GridChanged)
  }

  def redo: Unit = {
    undoManager.redoStep
    publish(new GridChanged)
  }

  override def getGameStatus() = gameStatus

  override def getGridRow: Int = grid.rows

  override def getGridCol: Int = grid.cols

}
