package streaming

import akka.actor.Actor
import common.{NeedlemanWunchConfig, Nucleotide}
import inmemory.InMemoryNeedlemanWunsch

import scala.collection.mutable.MutableList

/**
  * Created by dkettlestrings on 5/24/17.
  */
class InefficientNeedlemanWunschProcessor(conf: NeedlemanWunchConfig) extends Actor {

  require(conf.gap < 0)
  require(conf.mismatch < 0)
  require(conf.matching > 0)

  var seq1Active = true
  var seq2Active = true


  var sequence1: MutableList[Nucleotide] = MutableList.empty
  var sequence2: MutableList[Nucleotide] = MutableList.empty

  override def receive = {

    case (1, n: Nucleotide) => sequence1 += n
    case (2, n: Nucleotide) => sequence2 += n
    case (1, "close") => seq1Active = false
    case (2, "close") => seq2Active = false
    case "isActive" => sender ! (seq1Active || seq2Active)
    case "result" => sender ! InMemoryNeedlemanWunsch.inMem(sequence1.toIndexedSeq, sequence2.toIndexedSeq, conf)

  }



}
