package main.scala.model.gridComponent

import main.scala.model.gridComponent.gridBaseImpl.{Cell, Field, Matrix}

trait GridInterface {
  val cells: Matrix[Cell]

  def rows: Int

  def cols: Int

  def cell(row: Int, col: Int): Cell

  def set(row: Int, col: Int, value: Int): GridInterface

  def row(row: Int): Field

  def col(col: Int): Field

  def link_diagonal(row: Int, col: Int): Field

  def right_diagonal(row: Int, col: Int): Field

  def is4Stone(row: Int, col: Int, n: Int): Boolean
}

trait CellInterface {
  def value: Int

  def isSet: Boolean

  //  def set(value:Int): CellInterface
}