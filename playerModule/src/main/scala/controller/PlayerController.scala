package controller

import play.api.libs.json.{JsValue, Json}
import player.Player

class PlayerController() {

  var player1 = Player("Player1")
  var player2 = Player("Player2")
  var playerslist = Array(player1, player2)

  def rename(newname:String) = {
    if (playerslist(0).equals(player1)) {
      player1 = Player(newname)
      playerslist = Array(player1, player2)
    } else {
      player2 = Player(newname)
      playerslist(0) = player2
      playerslist = Array(player2, player1)
    }
  }

  def valueOfCurrentPlayer():Int = {
    if (playerslist(0).equals(player1)) {
      1
    } else {
      2
    }
  }

  def resetting = playerslist = Array(player1, player2)

  def reserve = playerslist = playerslist.reverse

  def playersToJson : JsValue = Json.obj(
    "player1" -> playerslist(0).toString,
    "player2"->playerslist(1).toString
  )
}
