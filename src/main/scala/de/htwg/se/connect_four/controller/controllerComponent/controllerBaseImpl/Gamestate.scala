package de.htwg.se.connect_four.controller.controllerComponent.controllerBaseImpl

case class Gamestate(var mystate:Statelike) {

  def changeState(): Unit = {
    mystate.handle(this)
  }
}
