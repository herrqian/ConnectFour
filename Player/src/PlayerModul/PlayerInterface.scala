package PlayerModul

trait PlayerInterface {
  val name:String
  def rename(newName: String): PlayerInterface
}
