package player.daoComponent.mongoDBImpl

import java.lang.Math.toIntExact
import java.time.Instant

import com.google.inject.Inject
import player.daoComponent.DAOInterface
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import org.bson.codecs.configuration.CodecRegistries._

import scala.concurrent.duration._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY

import scala.concurrent.Await



class MongoDBDao@Inject extends DAOInterface {

  private val  uri = "mongodb://127.0.0.1:27017/?&gssapiServiceName=mongodb"
  private val  client: MongoClient = MongoClient(uri)

  private val customCodecs = fromProviders(classOf[Player])
  private val codecRegistry = fromRegistries(customCodecs, DEFAULT_CODEC_REGISTRY)
  private val db: MongoDatabase = client.getDatabase("myDB").withCodecRegistry(codecRegistry)

  val players: MongoCollection[Player] = db.getCollection("Players")
  val time: Long = Instant.now.getEpochSecond
  val bar: Int = toIntExact(time)

  override def savePlayersList(player1: String, player2: String): Unit = {
    Await.result(players.insertOne(Player(bar, player1, player2)).toFuture(), 5 seconds)
  }

  override def loadLastPlayersList(): (Int, String, String) = {
    val allPlayers = Await.result(players.find().toFuture(), 5 seconds)
    allPlayers.foreach (
      c => {
        println(c)
        return (c.id, c.player1, c.player2)
      }
    )
    return null
  }


  case class Player(id: Int, player1: String, player2: String)
}
