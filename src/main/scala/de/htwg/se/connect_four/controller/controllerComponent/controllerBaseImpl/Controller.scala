package de.htwg.se.connect_four.controller.controllerComponent.controllerBaseImpl

import com.google.inject.name.Names
import com.google.inject.{Guice, Inject}
import net.codingwell.scalaguice.InjectorExtensions._
import de.htwg.se.connect_four.ConnectFourModule
import de.htwg.se.connect_four.controller.controllerComponent.GameStatus.GameStatus
import de.htwg.se.connect_four.model.gridComponent.GridInterface
import de.htwg.se.connect_four.model.gridComponent.gridBaseImpl.{Cell, Matrix, Field}
import de.htwg.se.connect_four.util.UndoManager
import de.htwg.se.connect_four.controller.controllerComponent.{CellChanged, ControllerInterface, GameStatus, GridChanged, GridSizeChanged, WinEvent}
import de.htwg.se.connect_four.model.fileIOComponent.FileIOInterface

class Controller @Inject()(var grid: GridInterface) extends ControllerInterface {

  var playerList = Array(true, false)
  var gameStatus: Gamestate = Gamestate(StatelikeIDLE(GameStatus.IDLE))
  private val undoManager = new UndoManager
  val injector = Guice.createInjector(new ConnectFourModule)
  val fileIo = injector.instance[FileIOInterface]
  val rowsCols = Map("gridrow" -> 6, "gridcol" -> 7)

  def save(): Unit = {
    fileIo.save(grid, playerList)
    publish(new GridChanged)
  }

  def load(): Unit = {
    val data = fileIo.load
    grid = data._1 match {
      case Some(g) => g
    }
    playerList = data._2
    publish(new GridSizeChanged("new size"))
    publish(new GridChanged)
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
    gameStatus = Gamestate(StatelikeIDLE(GameStatus.IDLE))
    publish(new GridSizeChanged(s))
  }


  def setValueToBottom(column: Int): Unit = {
    val value = if (playerList(0)) {
      1
    } else {
      2
    }
    val row = this.setValueR(grid.col(column), grid.cells.row - 1, column, value)
    this.changeTurn()
    publish(new CellChanged(row, column, value))
  }

  private def setValueR(cells: Field, row: Int, col: Int, stone: Int): Int = {
    if (cells.cell(row).equals(Cell(0))) {
      undoManager.doStep(new SetCommand(row, col, stone, this))
      row
    } else {
      setValueR(cells, row - 1, col, stone)
    }
  }

  def checkWinner(row: Int, col: Int, stone: Int): Unit = {
    if (grid.is4Stone(row, col, stone))
      publish(new WinEvent(stone))
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

  override def getGameStatus(): GameStatus = gameStatus.mystate.gameStatus

  override def getGridRow: Int = grid.rows

  override def getGridCol: Int = grid.cols

}
