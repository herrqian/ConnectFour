package player.daoComponent.slickImpl

import com.google.inject.Inject
import player.daoComponent.DAOInterface
import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape
import scala.concurrent.Await
import scala.util.{Failure, Success}
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class SlickDao @Inject extends DAOInterface {

  private val players = TableQuery[Players]
  val db = Database.forURL("jdbc:h2:mem:test1",
    driver = "org.h2.Driver",
    keepAliveConnection = true)
  db.run(DBIO.seq(players.schema.create))

  override def savePlayersList(player1: String, player2: String): Unit = {
    db.run(players += (0, player1, player2)).onComplete {
      case Success(_) => println("saved succeed")
      case Failure(_) => {
        Thread.sleep(1000)
        savePlayersList(player1, player2)
      }
    }
  }

  override def loadLastPlayersList(): (Int, String, String) = {
    val query = db.run(players.sortBy(_.id.desc).result.headOption)
    Await.result(query, Duration.Inf).get
  }
}

case class Players(tag: Tag) extends Table[(Int, String, String)](tag, "players") {
  def id: Rep[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def player1: Rep[String] = column[String]("Player1")

  def player2: Rep[String] = column[String]("Player2")

  def * : ProvenShape[(Int, String, String)] = (id, player1, player2)
}