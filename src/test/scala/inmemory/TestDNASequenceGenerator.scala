package inmemory

import org.scalatest.FunSuite

/**
  * Created by dkettlestrings on 5/24/17.
  */
class TestDNASequenceGenerator extends FunSuite {

  //TODO: non-deterministic test
  test("bounds are inclusive") {

    val seqs: IndexedSeq[DNASequence] = (0 to 45).map(_ => DNASequenceGenerator.generate(minLength = 2, maxLength = 5))

    seqs.foreach(s => assert(s.length >= 2 && s.length <= 5))

    assert(seqs.exists(_.length == 2))
    assert(seqs.exists(_.length == 5))
  }

}
