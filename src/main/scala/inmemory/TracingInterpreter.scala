package inmemory

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

/**
  * Created by dkettlestrings on 5/24/17.
  */
object TracingInterpreter {

  def interpret(seq1: DNASequence, seq2: DNASequence, traceback: Array[Array[TracebackEntry]]): String = {

    val mutable = toMutable(traceback)

    val optimalPath = getEvents(mutable, List.empty).reverse

    mkString(seq1, seq2, optimalPath)

  }

  @tailrec
  private def getEvents(traces: ArrayBuffer[ArrayBuffer[TracebackEntry]], acc: List[TracebackEntry]): List[TracebackEntry] = {

    val event = traces.last.last
    event match {
      case Done => acc
      case Left => getEvents(dropLastColumn(traces), acc :+ Left)
      case Up => getEvents(dropLastRow(traces), acc :+ Up)
      case Diagonal => getEvents(dropLastRow(dropLastColumn(traces)), acc:+ Diagonal)

    }

  }

  private def dropLastColumn(traceback: ArrayBuffer[ArrayBuffer[TracebackEntry]]): ArrayBuffer[ArrayBuffer[TracebackEntry]] = {

    traceback.zipWithIndex.foreach({case (_, i) =>
      traceback(i) = traceback(i).dropRight(1)
    })

    traceback
  }

  private def dropLastRow(traceback: ArrayBuffer[ArrayBuffer[TracebackEntry]]): ArrayBuffer[ArrayBuffer[TracebackEntry]] = {

    val result = traceback.dropRight(1)
    result

  }

  def mkString(seq1: DNASequence, seq2: DNASequence, events: List[TracebackEntry]): String = {

    var top = ""
    var bottom = ""

    var seq1Index = 0
    var seq2Index = 0

    events.foreach(event => event match {

      case Diagonal =>

        top = top + Nucleotide.asString(seq2(seq2Index))
        bottom = bottom + Nucleotide.asString(seq1(seq1Index))
        seq1Index += 1
        seq2Index += 1

      case Up =>

        top = top + "-"
        bottom = bottom + Nucleotide.asString(seq1(seq1Index))
        seq1Index += 1

      case Left =>

        top = top + Nucleotide.asString(seq2(seq2Index))
        bottom = bottom + "-"
        seq2Index +=1


      case Done => throw new IllegalArgumentException("Done should not have been one of the input traceback entities")

    })


    top + "\n" + bottom
  }

  private def toMutable(arr: Array[Array[TracebackEntry]]): ArrayBuffer[ArrayBuffer[TracebackEntry]] = {

    val result: ArrayBuffer[ArrayBuffer[TracebackEntry]] = ArrayBuffer.fill(arr.length, arr(0).length)(Done)

    arr.zipWithIndex.foreach({case (row, i) =>

      result(i) = row.to[ArrayBuffer]

    })

    result

  }

}
