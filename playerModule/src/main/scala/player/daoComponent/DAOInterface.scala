package player.daoComponent

trait DAOInterface {

  def savePlayersList(player1:String, player2:String):Unit

  def loadLastPlayersList():(Int,String,String)
}
