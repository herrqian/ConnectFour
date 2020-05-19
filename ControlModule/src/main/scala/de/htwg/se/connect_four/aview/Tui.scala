package de.htwg.se.connect_four.aview

import de.htwg.se.connect_four.controller.controllerComponent.{CellChanged, ControllerInterface, GridChanged, GridSizeChanged, LoadError, SaveError, SetError, WinEvent}

import scala.swing.Reactor
import scala.io.StdIn

class Tui(controller: ControllerInterface) extends Reactor {

  listenTo(controller)
  var winnerCheck = false
  var player1 = "Player1"
  var player2 = "Player2"
  var input = ""

  def processInputLineStart(): Unit = {
    processInputLineLoop()
  }

  def processInputLineLoop(): Unit = {
    do {
      if (winnerCheck) {
        println("Start a new game please!")
        input = StdIn.readLine()
        processInputLine(input)
        winnerCheck = false
      }
      println(controller.currentPlayer() + ", it is your turn!")
      input = StdIn.readLine() // stuck at this readline at the end of a gui-game
      processInputLine(input)
    } while (input != "q")
  }

  def processInputLine(input: String): Unit = {
    input match {
      case "q" => println("The game exit")
      case "n small" =>
        controller.resetPlayerList()
        controller.createEmptyGrid("Grid Small")
      case "n middle" =>
        controller.resetPlayerList()
        controller.createEmptyGrid("Grid Middle")
      case "n large" =>
        controller.resetPlayerList()
        controller.createEmptyGrid("Grid Large")
      case "undo" => controller.undo
      case "redo" => controller.redo
      case "save" => controller.save
      case "load" => controller.load
      case _ =>
        if (winnerCheck) {
          println("please start a new game")
          return
        }
        input.toList.filter(c => c != ' ') match {
          case 'i' :: column :: Nil =>
            controller.setValueToBottom(column.asDigit)
          case _ =>
            println("wrong input, repeat your turn!")
        }
    }
  }

  reactions += {
    case _: CellChanged => printTui()
    case _: GridSizeChanged => printTui()
    case _: GridChanged => printTui()
    case event: WinEvent => printWinner(event.winner)
    case event: LoadError => println("There is a LoadError: " + event.e)
    case event: SaveError => println("There is a SaveError: " + event.e)
    case event: SetError=>println("There is a SetError: " + event.e)
  }

  def printTui(): Unit = {
    println(controller.gridToString)
    //println(controller.getGameStatus())
  }

  def printWinner(winner: Int): Unit = {
    println(controller.gridToString)
    if (winner == 1) {
      printf("%s is the winner!\n", player1)
    } else {
      printf("%s is the winner!\n", player2)
    }
    winnerCheck = true
  }
}