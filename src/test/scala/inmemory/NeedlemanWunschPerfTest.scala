package inmemory

import org.scalatest.FunSuite
import Profiler.profile

/**
  * There are no stated performance performance requirements, so these tests currently assert nothing and just dump
  * results to the console.
  * Created by dkettlestrings on 5/24/17.
  */
class NeedlemanWunschPerfTest extends FunSuite {

  val conf = NeedlemanWunchConfig(matching = 5, mismatch = -1, gap = -2)

  test("Needleman-Wunsch at 200 pairs of length 20-50") {

    val pairs = (1 to 200).map(_ => DNASequenceGenerator.pair())

    val (_, time) = profile(pairs.foreach(tup => NeedlemanWunsch.inMem(tup._1, tup._2, conf)))

    println(s"time to run in-memory Needlman-Wunsch for 200 pairs of length 20-50 (in milliseconds): $time")
  }

  test("Needleman-Wunsch at 1000 pairs of length 20-50") {

    val pairs = (1 to 1000).map(_ => DNASequenceGenerator.pair())

    val (_, time) = profile(pairs.foreach(tup => NeedlemanWunsch.inMem(tup._1, tup._2, conf)))

    println(s"time to run in-memory Needlman-Wunsch for 1000 pairs of length 20-50 (in milliseconds): $time")

  }

  test("Needleman-Wunsch at 200 pairs of length 60-100") {

    val pairs = (1 to 10000).map(_ => DNASequenceGenerator.pair(minLength = 60, maxLength = 100))

    val (_, time) = profile(pairs.foreach(tup => NeedlemanWunsch.inMem(tup._1, tup._2, conf)))

    println(s"time to run in-memory Needlman-Wunsch for 200 pairs of length 60-100 (in milliseconds): $time")


  }

  test("Needleman-Wunsch at 1000 pairs of length 60-100") {

    val pairs = (1 to 10000).map(_ => DNASequenceGenerator.pair(minLength = 60, maxLength = 100))

    val (_, time) = profile(pairs.foreach(tup => NeedlemanWunsch.inMem(tup._1, tup._2, conf)))

    println(s"time to run in-memory Needlman-Wunsch for 1000 pairs of length 60-100 (in milliseconds): $time")


  }



}
