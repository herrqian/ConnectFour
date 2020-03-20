package de.htwg.se.connect_four.controller.controllerComponent.controllerBaseImpl

import com.google.inject.name.Names
import com.google.inject.{Guice, Inject}
import net.codingwell.scalaguice.InjectorExtensions._
import de.htwg.se.connect_four.ConnectFourModule
import de.htwg.se.connect_four.controller.controllerComponent.GameStatus.GameStatus
import de.htwg.se.connect_four.model.gridComponent.GridInterface
import de.htwg.se.connect_four.model.gridComponent.gridBaseImpl.Cell
import de.htwg.se.connect_four.util.UndoManager
import de.htwg.se.connect_four.controller.controllerComponent.{CellChanged, ControllerInterface, GameStatus, GridSizeChanged, WinEvent}
import de.htwg.se.connect_four.model.fileIOComponent.FileIOInterface

class Controller @Inject() (var grid: GridInterface) extends ControllerInterface {

  var playerList = Array(true, false)
  var gameStatus: Gamestate = Gamestate(StatelikeIDLE(GameStatus.IDLE))
  private val undoManager = new UndoManager
  val injector = Guice.createInjector(new ConnectFourModule)

  val fileIo = injector.instance[FileIOInterface]

  def save():Unit = {
    fileIo.save(grid, playerList)
    publish(new CellChanged)
  }

  def load():Unit =  {
    val data = fileIo.load
    grid = data._1
    playerList = data._2
    publish(new CellChanged)
  }

  var gridrow = 6
  var gridcol = 7

  def createEmptyGrid(s:String): Unit = {
    s match {
      case "Grid Small" =>{
        grid = injector.instance[GridInterface](Names.named(("Grid Small")))
        gridrow = 6
        gridcol = 7
      }
      case "Grid Middle" => {
        grid = injector.instance[GridInterface](Names.named(("Grid Middle")))
        gridrow = 10
        gridcol = 11
      }
      case "Grid Huge" => {
        grid = injector.instance[GridInterface](Names.named(("Grid Large")))
        gridrow = 16
        gridcol = 17
      }
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
    for (i <- grid.cells.row - 1 to 0 by -1) {
      if (grid.col(column).cell(i).equals(Cell(0))) {
        undoManager.doStep(new SetCommand(i,column, value, this))
        if (this.checkWinner(i, column)) {
          gameStatus.changeState()
          publish(new WinEvent)
          return
        } else {
          this.changeTurn()
          publish(new CellChanged)
          return
        }
      }
    }
  }

  def checkWinner(row: Int, col: Int): Boolean = {
    if (check4number(grid.col(col).getCells)) {
      true
    } else if (check4number(grid.row(row).getCells)) {
      true
    } else if (check4number(grid.link_diagonal(row, col).getCells)) {
      true
    } else if (check4number(grid.right_diagonal(row, col).getCells)) {
      true
    } else {
      false
    }
  }

  private def check4number(vector: Vector[Cell]): Boolean = {
    var counter = 0
    for (cell <- vector) {
      if (cell.equals(Cell(currentPlayer()))) {
        counter = counter + 1
        if (counter == 4) {
          return true
        }
      } else {
        counter = 0
      }
    }
    false
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

  def resetPlayerList():Unit= {
    playerList = Array(true,false)
  }

  def gridToString: String = grid.toString

  def undo: Unit = {
    undoManager.undoStep
    publish(new CellChanged)
  }

  def redo: Unit ={
    undoManager.redoStep
    publish(new CellChanged)
  }

  override def getGameStatus(): GameStatus = gameStatus.mystate.gameStatus

  override def getGridRow: Int = gridrow

  override def getGridCol: Int = gridcol
}
