package main.scala.aview

import controller._

import scala.io.StdIn
import scala.swing.Reactor

class Tui(controller: ControllerInterface) extends Reactor {

  listenTo(controller)
  var winnerCheck = false
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
      printTui()
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
        controller.createEmptyGrid("GridSmall")
      case "n middle" =>
        controller.resetPlayerList()
        controller.createEmptyGrid("GridMiddle")
      case "n large" =>
        controller.resetPlayerList()
        controller.createEmptyGrid("GridLarge")
      case "undo" => controller.undo
      case "redo" => controller.redo
      case "save" => controller.save
      case "load" => controller.load
      case _ =>
        if (input.startsWith("rename")) {
          controller.renamePlayer(input.replaceAll("rename ",""))
          return
        }
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
    case _: WinEvent => printWinner()
    case event: LoadError => println("There is a LoadError: " + event.e)
    case event: SaveError => println("There is a SaveError: " + event.e)
    case event: SetError=>println("There is a SetError: " + event.e)
  }

  def printTui(): Unit = {
    println(controller.gridToString)
    //println(controller.getGameStatus())
  }

  def printWinner(): Unit = {
    println(controller.gridToString)
    printf("%s is the winner!\n", controller.currentPlayer())
    winnerCheck = true
  }
}