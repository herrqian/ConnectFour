package de.htwg.se.connect_four.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.connect_four.controller.controllerComponent.GameStatus
import de.htwg.se.connect_four.controller.controllerComponent.GameStatus.GameStatus

case class StatelickWIN(var gameStatus: GameStatus) extends Statelike {
  override def handle(gamestate: Gamestate): Unit = {
    gamestate.mystate = StatelickWIN(GameStatus.IDLE)
  }
}
