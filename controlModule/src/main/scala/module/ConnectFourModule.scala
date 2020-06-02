package scala.module

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import controller.ControllerInterface
import main.scala.controller.controllerBaseImpl.Controller
import main.scala.fileIOComponent.FileIOInterface
import main.scala.fileIOComponent.fileIoJsonImpl.FileIO
import main.scala.model.gridComponent.GridInterface
import main.scala.model.gridComponent.gridAdvancedImpl.Grid
import net.codingwell.scalaguice.ScalaModule


class ConnectFourModule extends AbstractModule with ScalaModule {

  val defaultRows:Int = 6
  val defaultCols:Int = 7

  override def configure(): Unit = {
    bindConstant().annotatedWith(Names.named("DefaultRows")).to(defaultRows)
    bindConstant().annotatedWith(Names.named("DefaultCols")).to(defaultCols)
    bind[GridInterface].to[Grid]
    bind[ControllerInterface].to[Controller]
    bind[GridInterface].annotatedWithName("Grid Small").toInstance(new Grid(6,7))
    bind[GridInterface].annotatedWithName("Grid Middle").toInstance(new Grid(10,11))
    bind[GridInterface].annotatedWithName("Grid Large").toInstance(new Grid(16,17))

    bind[FileIOInterface].to[FileIO]
  }
}
