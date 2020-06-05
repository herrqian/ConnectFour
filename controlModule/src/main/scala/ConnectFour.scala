package scala

import com.google.inject.Guice
import main.scala.aview.{HttpServer, Tui}
//import main.scala.aview.gui.SwingGui
import main.scala.controller.controllerBaseImpl.Controller
import main.scala.model.gridComponent.gridAdvancedImpl.Grid
import aview.{GridMain, PlayerMain}

import scala.module.ConnectFourModule

object ConnectFour {
  val injector = Guice.createInjector(new ConnectFourModule)
  val controller = new Controller()
  val tui = new Tui(controller)
  //val gui = new SwingGui(controller)
  val webserver = new HttpServer(controller)

   def main(args: Array[String]): Unit = {
    tui.processInputLineStart()
    webserver.unbind
  }
}