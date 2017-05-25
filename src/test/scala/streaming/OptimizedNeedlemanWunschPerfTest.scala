package streaming

import common.Profiler.profile
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import common._
import org.scalatest.{FunSuite, Ignore}

import scala.concurrent.Await
import scala.concurrent.duration._
import language.postfixOps

/**
  * There are no stated performance performance requirements, so these tests currently assert nothing and just dump
  * results to the console.
  *
  * Created by dkettlestrings on 5/25/17.
  */
@Ignore
class OptimizedNeedlemanWunschPerfTest extends FunSuite {

  implicit val timeout = Timeout(45 seconds)

  def poll(processor: ActorRef): String = {

    val isActive = Await.result(processor ? "isActive", Duration(5, "seconds")).asInstanceOf[Boolean]
    if(isActive) {Thread.sleep(500); poll(processor)} else Await.result(processor ? "result", Duration(45, "seconds")).asInstanceOf[String]
  }

  test("Optimized Needleman-Wunsch at 200 pairs of length 20-50") {

    val pairs = (1 to 200).map(_ => DNASequenceGenerator.pair())

    val system = ActorSystem("OptimizedNeedlemanWunschPerfTest")
    val conf = NeedlemanWunchConfig(matching = 5, mismatch = -1, gap = -2)

    val (_, time) = profile(pairs.foreach({case (seq1, seq2) => {

      val processor = system.actorOf(Props(classOf[OptimizedNeedlemanWunschProcessor], conf))
      val stream1 = system.actorOf(Props(classOf[FixedSequenceGenerator], processor, 1, seq1))
      val stream2 = system.actorOf(Props(classOf[FixedSequenceGenerator], processor, 2, seq2))
      stream1 ! "go"
      stream2 ! "go"
      poll(processor)
      println("!")
    }}))

    println(s"time to run optimized Needleman-Wunsch on 200 pairs of length 20-50 (in milliseconds)=$time")

    system.terminate()

  }

}
