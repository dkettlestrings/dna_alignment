package inmemory

import scala.util.Random

/**
  * Created by dkettlestrings on 5/24/17.
  */
object DNASequenceGenerator {

  def generate(minLength: Int = 20, maxLength: Int = 50): DNASequence = {

    require(minLength > 0)
    require(maxLength > minLength)

    val length: Int = minLength + Random.nextInt(maxLength - minLength + 1)

    (1 to length).map(_ => Nucleotide.next())

  }

  def pair(minLength: Int = 20, maxLength: Int = 50): (DNASequence, DNASequence) = {

    (generate(minLength, maxLength), generate(minLength, maxLength))
  }

}
