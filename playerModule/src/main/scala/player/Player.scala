package player

case class Player(name:String) extends PlayerInterface {

  def rename(newName: String): PlayerInterface =copy(newName)

  override def toString: String = name
}
