package de.htwg.se.connect_four.model.gridComponent.gridAdvancedImpl

import de.htwg.se.connect_four.model.gridComponent.gridBaseImpl.{Grid=>BaseGrid}
import com.google.inject.Inject
import com.google.inject.name.Named

class Grid @Inject() (@Named("DefaultRows")row:Int,@Named("DefaultCols")col:Int) extends BaseGrid(row,col) {
}

