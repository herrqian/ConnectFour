package scala.de.htwg.se.connect_four.model

import PlayerModul.Player
import org.scalatest.{Matchers, WordSpec}

class PlayerSpec extends WordSpec with Matchers {
  "A PlayerModul.Player" when {
    "new" should {
      val name ="Your Name"
      val player = Player(name)
      "have a name" in {
        player.name should be("Your Name")
      }
      "have a nice String representation" in {
        player.toString should be("Your Name")
      }
    }
  }
}
