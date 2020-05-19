package main.scala.model.gridComponent.gridAdvancedImpl

import com.google.inject.Inject
import com.google.inject.name.Named
import main.scala.model.gridComponent.gridBaseImpl.{Grid => BaseGrid}

class Grid @Inject() (@Named("DefaultRows")row:Int,@Named("DefaultCols")col:Int) extends BaseGrid(row,col) {
}

