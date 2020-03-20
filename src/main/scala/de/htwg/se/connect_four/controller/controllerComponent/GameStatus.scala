package de.htwg.se.connect_four.controller.controllerComponent

object GameStatus extends Enumeration {
  type GameStatus = Value
  val IDLE, WIN, PLAYER1TURN, PLAYER2TURN= Value

  val map = Map[GameStatus, String](
    IDLE -> "",
    WIN -> "Game over",
  )

  def message(gameStatus: GameStatus)  ={
    map(gameStatus)
  }
}
