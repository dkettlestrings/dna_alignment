package inmemory

import org.scalatest.FunSuite

/**
  * Created by dkettlestrings on 5/24/17.
  */
class TestNeedlemanWunsch extends FunSuite {

  test("works for sequences of the same size with a diagonal trace") {

    val seq1 = IndexedSeq(T, C, A, T, A)
    val seq2 = IndexedSeq(T, C, C, T, A)

    val conf = NeedlemanWunchConfig(matching = 5, mismatch = -1, gap = -2)

    val result = NeedlemanWunsch.inMem(seq1, seq2, conf)

    val expectedScores = Array(
      Array(0, -2, -4, -6, -8, -10),
      Array(-2, 5, 3, 1, -1, -3),
      Array(-4, 3, 10, 8, 6, 4),
      Array(-6, 1, 8, 9, 7, 11),
      Array(-8, -1, 6, 7, 14, 12),
      Array(-10, -3, 4, 5, 12, 19)
    )

    val expectedTraceback = Array(
      Array(Done, Left, Left, Left, Left, Left),
      Array(Up, Diagonal, Left, Left, Diagonal, Left),
      Array(Up, Up, Diagonal, Diagonal, Left, Left),
      Array(Up, Up, Up, Diagonal, Diagonal, Diagonal),
      Array(Up, Diagonal, Up, Diagonal, Diagonal, Left),
      Array(Up, Up, Up, Diagonal, Up, Diagonal)
    )


    assert(result.scores.deep == expectedScores.deep)
    assert(result.traceback.deep == expectedTraceback.deep)

    assert(result.report == "TCCTA\nTCATA")

  }

  test("should work for sequences of the same length with non-diagonal trace") {

    val seq1 = IndexedSeq(G, C, A, T, G, C, U)

    val seq2 = IndexedSeq(G, A, T, T, A, C, A)

    val conf = NeedlemanWunchConfig(matching = 1, mismatch = -1, gap = -1)

    val result = NeedlemanWunsch.inMem(seq1, seq2, conf)

    assert(result.report == "G-ATTACA\nGCA-TGCU")

  }

  test("should work for sequences of different length") {

    val seq1 = IndexedSeq(T, A, T, C, G)

    val seq2 = IndexedSeq(A, T, A, G)

    val conf = NeedlemanWunchConfig(matching = 5, mismatch = -1, gap = -2)

    val result = NeedlemanWunsch.inMem(seq1, seq2, conf)

    assert(result.report == "-ATAG\nTATCG")
  }


}
