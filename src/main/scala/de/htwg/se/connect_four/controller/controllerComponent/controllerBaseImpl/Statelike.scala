package de.htwg.se.connect_four.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.connect_four.controller.controllerComponent.GameStatus.GameStatus

trait Statelike {
  var gameStatus:GameStatus
  def handle(gamestate: Gamestate): Unit
}
