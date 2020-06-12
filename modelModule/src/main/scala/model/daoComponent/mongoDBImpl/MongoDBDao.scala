package model.daoComponent.mongoDBImpl

import com.google.inject.Inject
import model.daoComponent.DAOInterface
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import org.bson.codecs.configuration.CodecRegistries._
import org.mongodb.scala.bson.BsonObjectId

import scala.concurrent.duration._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY

import scala.concurrent.Await



class MongoDBDao@Inject extends DAOInterface {

  private val uri = "mongodb://127.0.0.1:27017/?&gssapiServiceName=mongodb"
  private val client: MongoClient = MongoClient(uri)

  private val customCodecs = fromProviders(classOf[Grid])
  private val codecRegistry = fromRegistries(customCodecs, DEFAULT_CODEC_REGISTRY)
  private val db: MongoDatabase = client.getDatabase("myDB").withCodecRegistry(codecRegistry)

  private val grids: MongoCollection[Grid] = db.getCollection("Grids")


  override def saveGrid(rows: Int, cols: Int, grid: String): Unit = {
    Await.result(grids.insertOne(Grid(0,rows,cols,grid)).toFuture(), 5 seconds)

  }

  override def loadLastGrid(): (Int, Int, Int, String) = {
    val allGrids = Await.result(grids.find().toFuture(), 5 seconds)
    allGrids.foreach (
      c => {
        println(c)
        return (c.id, c.rows, c.cols, c.grid)
      }
    )
    return null
  }

  case class Grid(id: Int, rows:Int, cols:Int, grid:String)
}