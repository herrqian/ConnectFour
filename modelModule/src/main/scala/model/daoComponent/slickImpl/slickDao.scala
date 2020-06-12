package model.daoComponent.slickImpl

import com.google.inject.Inject
import model.daoComponent.DAOInterface
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class SlickDao@Inject() extends DAOInterface{

  private val grids = TableQuery[Grids]
  val db = Database.forURL("jdbc:h2:mem:test1",
    driver ="org.h2.Driver",
    keepAliveConnection = true)
  db.run(DBIO.seq(grids.schema.create))

  override def saveGrid(rows:Int,cols:Int,grid:String): Unit = {
    db.run(grids += (0,rows,cols,grid))
  }


  override def loadLastGrid(): (Int, Int, Int, String) = {
    val query = db.run(grids.sortBy(_.id.desc).result.headOption)
    Await.result(query, Duration.Inf).get
  }
}


case class Grids(tag: Tag) extends Table[(Int, Int,Int,String)](tag, "Grids") {
  def id :Rep[Int] = column[Int]("ID",O.PrimaryKey,O.AutoInc)
  def rows: Rep[Int] = column[Int]("ROWS")
  def cols: Rep[Int] = column[Int]("COLS")
  def grid: Rep[String] = column[String]("GRID")

  override def * = (id, rows,cols,grid)
}