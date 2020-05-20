

val m = Matrix(Vector(Vector(Cell(0), Cell(1)), Vector(Cell(1), Cell(0))))

println(m)

val n = Vector(Vector(Cell(0), Cell(1)), Vector(Cell(1), Cell(0)))

val concatVec: (List[Cell], List[Cell]) => String = (s1, s2) => s1.fold("")(_ + " " + _) + System.lineSeparator() +s2.fold("")(_ + " " + _)
val concatDonuts: (Cell, Cell) => String = (s1, s2) => s1+ " " + s2

var s = ""
n.foreach(
  n1 => {
    s += n1.fold("")(_ + " " + _) + System.lineSeparator()
  }
)
println(s)
println("a")