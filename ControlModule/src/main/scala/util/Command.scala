package de.htwg.se.connect_four.util

trait Command {
  def doStep:Unit
  def undoStep:Unit
  def redoStep:Unit
}
