package aview

import controller.PlayerController

object PlayerMain {
    val playercontroller = new PlayerController()
    val httpserver = new PlayerHttpServer(playercontroller)
    @volatile var shutdown = false

    def main(args: Array[String]): Unit = {
      println("http://localhost:22222" + " is open")
      while (!shutdown) {
        Thread.sleep(1000)
        ;
      }

      httpserver.unbind
      println("http://localhost:22222" + " is closed")
    }

    def shutdownServer(): Unit = shutdown = true
}
