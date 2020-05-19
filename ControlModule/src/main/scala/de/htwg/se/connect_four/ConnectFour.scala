package scala.de.se.ConnectFour

import com.google.inject.Guice
import de.htwg.se.connect_four.aview.Tui
import de.htwg.se.connect_four.aview.gui.SwingGui
import de.htwg.se.connect_four.controller.controllerComponent.controllerBaseImpl.Controller
import main.scala.model.gridComponent.gridAdvancedImpl.Grid

import scala.de.htwg.se.connect_four.module.ConnectFourModule

object ConnectFour {
    val injector = Guice.createInjector(new ConnectFourModule)
    val controller = new Controller(new Grid(6,7))
    val tui = new Tui(controller)
    val gui = new SwingGui(controller)

    def main(args: Array[String]): Unit = {
      tui.processInputLineStart()
    }
}