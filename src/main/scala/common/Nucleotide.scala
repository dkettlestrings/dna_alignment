package common

import scala.util.Random


/**
  * Created by dkettlestrings on 5/24/17.
  */
sealed trait Nucleotide {

  val asString: String
}

object A extends Nucleotide {
  override val asString = "A"
}
object T extends Nucleotide {
  override val asString = "T"
}
object G extends Nucleotide {
  override val asString = "G"
}
object C extends Nucleotide {
  override val asString = "C"
}
object U extends Nucleotide {
  override val asString = "U"
}


//TODO: Use reflection/macros to get all Nucleotides
object Nucleotide {

  def next(): Nucleotide = lookup(Random.nextInt(5))

  private val lookup: Map[Int, Nucleotide] = Map(0 -> A, 1 -> T, 2 -> G, 3-> C, 4 -> U)

}


