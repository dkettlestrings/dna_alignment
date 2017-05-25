package inmemory

import scala.util.Random


/**
  * Created by dkettlestrings on 5/24/17.
  */
sealed trait Nucleotide

object A extends Nucleotide
object T extends Nucleotide
object G extends Nucleotide
object C extends Nucleotide
object U extends Nucleotide


//TODO: perhaps better to make this a method on the Nucleotide trait
//TODO: Use reflection/macros to get all Nucleotides
object Nucleotide {

  def asString(n: Nucleotide): String = n match {

    case A => "A"
    case T => "T"
    case G => "G"
    case C => "C"
    case U => "U"
  }

  val all: Set[Nucleotide] = Set(A, T, G, C)

  //TODO: use this in in-memory and rip out of DNASequenceGenerator
  def next(): Nucleotide = lookup(Random.nextInt(5))

  private val lookup: Map[Int, Nucleotide] = Map(0 -> A, 1 -> T, 2 -> G, 3-> C, 4 -> U)

}


