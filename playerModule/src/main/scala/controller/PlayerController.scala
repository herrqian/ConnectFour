package controller

import aview.PlayersModule
import com.google.inject.{Guice, Injector}
import play.api.libs.json.{JsValue, Json}
import player.Player
import player.daoComponent.DAOInterface
import player.daoComponent.slickImpl.SlickDao
import player.daoComponent.mongoDBImpl.MongoDBDao

class PlayerController() {

  val injector: Injector = Guice.createInjector(new PlayersModule)
  val db: DAOInterface = new SlickDao
  var player1 = Player("Player1")
  var player2 = Player("Player2")
  var playerslist = Array(player1, player2)

  def rename(newname: String) = {
    if (playerslist(0).equals(player1)) {
      player1 = Player(newname)
      playerslist = Array(player1, player2)
    } else {
      player2 = Player(newname)
      playerslist(0) = player2
      playerslist = Array(player2, player1)
    }
  }

  def valueOfCurrentPlayer(): Int = {
    if (playerslist(0).equals(player1)) {
      1
    } else {
      2
    }
  }

  def save() = {
    db.savePlayersList(playerslist(0).toString, playerslist(1).toString)
  }

  def load() = {
    val result = db.loadLastPlayersList
    val aplayer1 = Player(result._2.replace("\"",""))
    val aplayer2 = Player(result._3.replace("\"",""))
    playerslist = Array(aplayer1,aplayer2)
  }

  def resetting = playerslist = Array(player1, player2)

  def reserve = playerslist = playerslist.reverse

  def playersToJson: JsValue = Json.obj(
    "player1" -> playerslist(0).toString,
    "player2" -> playerslist(1).toString
  )
}
