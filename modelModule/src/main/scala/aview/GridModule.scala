package aview

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import main.scala.fileIOComponent.FileIOInterface
import main.scala.fileIOComponent.fileIoJsonImpl.FileIO
import main.scala.model.gridComponent.GridInterface
import main.scala.model.gridComponent.gridAdvancedImpl.Grid
import model.daoComponent.DAOInterface
import model.daoComponent.mongoDBImpl.MongoDBDao
import net.codingwell.scalaguice.ScalaModule
import model.daoComponent.slickImpl

class GridModule extends AbstractModule with ScalaModule {

  val defaultRows:Int = 6
  val defaultCols:Int = 7

  override def configure(): Unit = {
    bindConstant().annotatedWith(Names.named("DefaultRows")).to(defaultRows)
    bindConstant().annotatedWith(Names.named("DefaultCols")).to(defaultCols)
    bind[GridInterface].to[Grid]
    bind[FileIOInterface].to[FileIO]
    bind[DAOInterface].to[MongoDBDao]
  }


}
