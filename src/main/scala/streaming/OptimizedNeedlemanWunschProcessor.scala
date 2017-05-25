package streaming

import akka.actor.Actor
import common._
import inmemory._

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by dkettlestrings on 5/25/17.
  */
class OptimizedNeedlemanWunschProcessor(conf: NeedlemanWunchConfig) extends Actor {

  case class Entry(score: Int, traceback: List[TracebackEntry])

  require(conf.gap < 0)
  require(conf.mismatch < 0)
  require(conf.matching > 0)

  var seq1Active = true
  var seq2Active = true

  var previousRow: ArrayBuffer[Entry] = ArrayBuffer.empty
  var currentRow: ArrayBuffer[Entry] = ArrayBuffer(Entry(0, List(Done)))

  var currentColumn: ArrayBuffer[Entry] = ArrayBuffer(Entry(0, List(Done)))

  var sequence1: mutable.MutableList[Nucleotide] = mutable.MutableList.empty
  var sequence2: mutable.MutableList[Nucleotide] = mutable.MutableList.empty

  override def receive = {

    case (1, n: Nucleotide) => {sequence1 = sequence1 += n; addRow(n)}
    case (2, n: Nucleotide) => {sequence2 = sequence2 += n; addColumn(n)}
    case (1, "close") => seq1Active = false
    case (2, "close") => seq2Active = false
    case "isActive" => sender ! (seq1Active || seq2Active)
    case "result" => sender ! TracingInterpreter.mkString(sequence1.toIndexedSeq, sequence2.toIndexedSeq, currentRow.last.traceback.init.reverse)
  }

  private def addRow(n: Nucleotide): Unit = {

    var newRow: ArrayBuffer[Entry] = ArrayBuffer.empty

    currentRow.zipWithIndex.foreach({case (entry, i) =>

      val upScore = entry.score + conf.gap
      val leftScore = if(i == 0) Integer.MIN_VALUE else newRow(i - 1).score + conf.gap
      val diagonalScore = if(i ==0) Integer.MIN_VALUE else {

        val matchScore = if(sequence2(i - 1) == n) conf.matching else conf.mismatch
        currentRow(i - 1).score + matchScore

      }

      val newEntry = if(diagonalScore >= upScore && diagonalScore >= leftScore) {

        Entry(diagonalScore, Diagonal +: currentRow(i - 1).traceback)
      }
      else if(leftScore >= diagonalScore && leftScore >= upScore) {

        Entry(leftScore, common.Left +: newRow(i - 1).traceback)
      }
      else {

        Entry(upScore, Up +: entry.traceback)
      }

      newRow = newRow += newEntry

      if(i == currentRow.length - 1) {
        currentColumn = currentColumn += newEntry
      }



    })

    //Swap active rows
    previousRow = currentRow
    currentRow = newRow

  }

  private def addColumn(n: Nucleotide): Unit = {

    var newColumn: ArrayBuffer[Entry] = ArrayBuffer.empty

    currentColumn.zipWithIndex.foreach({case(entry, i) =>

      val upScore = if(i == 0) Integer.MIN_VALUE else newColumn(i - 1).score + conf.gap
      val leftScore = entry.score + conf.gap
      val diagonalScore = if(i == 0) Integer.MIN_VALUE else {

        val matchScore = if(sequence1(i - 1) == n) conf.matching else conf.mismatch
        currentColumn(i - 1).score + matchScore

      }

      val newEntry = if(diagonalScore >= upScore && diagonalScore >= leftScore) {

        Entry(diagonalScore, Diagonal +: currentColumn(i - 1).traceback)
      }
      else if(leftScore >= diagonalScore && leftScore >= upScore) {

        Entry(leftScore, common.Left +: entry.traceback)
      }
      else {

        Entry(upScore, Up +: newColumn(i - 1).traceback)
      }

      newColumn = newColumn += newEntry

      if(i == currentColumn.length - 2) {
        previousRow = previousRow += newEntry
      }
      if(i == currentColumn.length - 1) {
        currentRow = currentRow += newEntry
      }

    })

    //Swap out columns
    currentColumn = newColumn


  }

}
