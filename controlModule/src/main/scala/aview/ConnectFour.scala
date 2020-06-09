package aview

import com.google.inject.Guice
import main.scala.aview.{HttpServer, Tui}
import main.scala.controller.controllerBaseImpl.Controller

import scala.module.ConnectFourModule

object ConnectFour {
  val injector = Guice.createInjector(new ConnectFourModule)
  val controller = new Controller()
  val tui = new Tui(controller)
  //val gui = new SwingGui(controller)
  val webserver = new HttpServer(controller)

  println("GRID_HOST is ",controller.gridHost)
  println("PLAYER_HOST is ",controller.playerHost)

   def main(args: Array[String]): Unit = {
    tui.processInputLineStart()
    webserver.unbind
  }
}
