package main.scala.controller

import main.scala.controller.GameStatus.GameStatus
import main.scala.model.gridComponent.GridInterface
import player.Player

import scala.swing.Publisher

trait ControllerInterface extends Publisher {
  def createEmptyGrid(s: String): Unit

  def setValueToBottom(col: Int): Unit

  def checkWinner(row: Int, col: Int, stone: Int): Boolean

  def getTurn(playerNumber: Int): Player

  def changeTurn(): Unit

  def currentPlayer(): Player

  def gridToString: String

  def resetPlayerList(): Unit

  def renamePlayer(newname:String):Unit

  def getGameStatus(): GameStatus

  def undo: Unit

  def redo: Unit

  def save: Unit

  def load: Unit

  def getGridRow: Int

  def getGridCol: Int

  var grid: GridInterface

}

import scala.swing.event.Event

class CellChanged(val row: Int, val col: Int, val stone: Int) extends Event

class GridSizeChanged(size: String) extends Event

class WinEvent(val winner: Int) extends Event

class GridChanged extends Event

class LoadError(val e: String) extends Event

class SaveError(val e: String) extends Event

class SetError(val e: String) extends Event