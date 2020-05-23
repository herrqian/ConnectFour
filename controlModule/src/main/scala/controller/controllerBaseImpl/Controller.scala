package main.scala.controller.controllerBaseImpl

import com.google.inject.name.Names
import com.google.inject.{Guice, Inject}
import de.htwg.se.connect_four.util.UndoManager
import main.scala.controller.{CellChanged, ControllerInterface, GridChanged, GridSizeChanged, LoadError, SaveError, SetError, WinEvent}
import main.scala.model.gridComponent.GridInterface
import main.scala.model.gridComponent.gridBaseImpl.{Cell, Field}
import main.scala.controller.GameStatus.{IDLE, WIN}
import main.scala.fileIOComponent.FileIOInterface
import main.scala.fileIOComponent.fileIoJsonImpl.FileIO
import main.scala.model.gridComponent.gridAdvancedImpl.Grid
import player.Player

import scala.module.ConnectFourModule
import scala.util.{Failure, Success, Try}

class Controller @Inject()(var grid: GridInterface) extends ControllerInterface {

  var player1 = Player("Player1")
  var player2 = Player("Player2")
  var playerlist = Array(player1, player2)
  var gameStatus = IDLE
  private val undoManager = new UndoManager
  //val injector = Guice.createInjector(new ConnectFourModule)
  val fileIo = new FileIO
  val rowsCols = Map("gridrow" -> 6, "gridcol" -> 7)

  def save(): Unit = {
    fileIo.save(grid, playerlist) match {
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
        playerlist = data._2
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
        grid = new Grid(6,7)
      case Grids.middle =>
        grid = new Grid(10,11)
      case Grids.large =>
        grid = new Grid(16,17)
    }
    resetPlayerList()
    gameStatus = IDLE
    publish(new GridSizeChanged(s))
  }


  def setValueToBottom(column: Int): Unit = {
    val value = if (playerlist(0).equals(player1)) {
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
          println(playerlist)
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

  def getTurn(playerNumber: Int): Player = {
    playerlist(playerNumber)
  }

  def changeTurn(): Unit = {
    playerlist = playerlist.reverse
  }

  def currentPlayer(): Player = playerlist(0)

  def resetPlayerList(): Unit = {
    playerlist = Array(Player("Player1"), Player("Player2"))
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

  override def renamePlayer(newname:String): Unit = {
    playerlist(0) = Player(newname)
  }
  override def getGameStatus() = gameStatus

  override def getGridRow: Int = grid.rows

  override def getGridCol: Int = grid.cols

}
