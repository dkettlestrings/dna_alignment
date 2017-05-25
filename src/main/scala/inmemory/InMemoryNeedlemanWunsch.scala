package inmemory

import common._

/**
  * Created by dkettlestrings on 5/24/17.
  */
object InMemoryNeedlemanWunsch {

  def inMem(seq1: DNASequence, seq2: DNASequence, conf: NeedlemanWunchConfig): InMemoryNeedlemanWunchResult = {

    require(conf.gap < 0)
    require(conf.mismatch < 0)
    require(conf.matching > 0)

    // Create scoring and traceback matrices
    val scores = Array.ofDim[Int](seq1.length + 1, seq2.length + 1)
    val traceback = Array.ofDim[TracebackEntry](seq1.length + 1, seq2.length + 1)


    //Initialize first row and column for both matrices
    scores(0)(0) = 0
    traceback(0)(0) = Done

    (1 to seq1.length).foreach(i => scores(i)(0) = conf.gap * i)
    (1 to seq1.length).foreach(i => traceback(i)(0) = Up)

    (1 to seq2.length).foreach(j => scores(0)(j) = conf.gap * j)
    (1 to seq2.length).foreach(j => traceback(0)(j) = common.Left)

    // Go through each row and column and update the score and traceback matrix
    for {
      i <- 1 to seq1.length
      j <- 1 to seq2.length
    } yield update(row = i, column = j, scores, traceback)


    def update(row: Int, column: Int, scores: Array[Array[Int]], traceback: Array[Array[TracebackEntry]]): Unit = {


      val leftScore = scores(row)(column - 1) + conf.gap
      val upScore = scores(row - 1)(column) + conf.gap

      val matchingScore = if(seq1(row - 1) == seq2(column - 1)) conf.matching else conf.mismatch
      val diagonalScore = scores(row - 1)(column - 1) + matchingScore

      if(biggerThan(diagonalScore)(upScore, leftScore)) {

        scores(row)(column) = diagonalScore
        traceback(row)(column) = Diagonal
      }
      else if(biggerThan(leftScore)(diagonalScore, upScore)) {

        scores(row)(column) = leftScore
        traceback(row)(column) = common.Left
      }
      else {

        scores(row)(column) = upScore
        traceback(row)(column) = Up
      }

    }

    def biggerThan(a: Int)(b: Int, c: Int): Boolean = a >= b && a>=c


    InMemoryNeedlemanWunchResult(scores, traceback, TracingInterpreter.interpret(seq1, seq2, traceback))


  }

}
