package streaming

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask
import common._
import org.scalatest.FunSuite

import scala.concurrent.duration._
import language.postfixOps
import scala.concurrent.Await

/**
  * Created by dkettlestrings on 5/25/17.
  */
class TestOptimizedNeedlemanWunschProcessor extends FunSuite {

  implicit val timeout = Timeout(45 seconds)

  def poll(processor: ActorRef): String = {

    val isActive = Await.result(processor ? "isActive", Duration(5, "seconds")).asInstanceOf[Boolean]
    if(isActive) {Thread.sleep(500); poll(processor)} else Await.result(processor ? "result", Duration(45, "seconds")).asInstanceOf[String]
  }

  test("works for sequences of the same size with a diagonal trace") {

    val system = ActorSystem("TestFancyNeedlemanWunsch-1")
    val conf = NeedlemanWunchConfig(matching = 5, mismatch = -1, gap = -2)

    val processor = system.actorOf(Props(classOf[OptimizedNeedlemanWunschProcessor], conf))

    val stream1 = system.actorOf(Props(classOf[FixedSequenceGenerator], processor, 1, IndexedSeq(T, C, A, T, A)))
    val stream2 = system.actorOf(Props(classOf[FixedSequenceGenerator], processor, 2, IndexedSeq(T, C, C, T, A)))

    stream1 ! "go"
    stream2 ! "go"

    val result = poll(processor)

    assert(result == "TCCTA\nTCATA")

    system.terminate()

  }

  test("should work for sequences of the same length with non-diagonal trace") {

    val system = ActorSystem("TestFancyNeedlemanWunsch-2")
    val conf = NeedlemanWunchConfig(matching = 1, mismatch = -1, gap = -1)

    val processor = system.actorOf(Props(classOf[OptimizedNeedlemanWunschProcessor], conf))

    val stream1 = system.actorOf(Props(classOf[FixedSequenceGenerator], processor, 1, IndexedSeq(G, C, A, T, G, C, U)))
    val stream2 = system.actorOf(Props(classOf[FixedSequenceGenerator], processor, 2, IndexedSeq(G, A, T, T, A, C, A)))

    stream1 ! "go"
    stream2 ! "go"

    val result = poll(processor)

    assert(result == "G-ATTACA\nGCA-TGCU")

    system.terminate()

  }

  test("should work for sequences of different length") {

    val system = ActorSystem("TestFancyNeedlemanWunsch-3")
    val conf = NeedlemanWunchConfig(matching = 5, mismatch = -1, gap = -2)

    val processor = system.actorOf(Props(classOf[OptimizedNeedlemanWunschProcessor], conf))

    val stream1 = system.actorOf(Props(classOf[FixedSequenceGenerator], processor, 1, IndexedSeq(T, A, T, C, G)))
    val stream2 = system.actorOf(Props(classOf[FixedSequenceGenerator], processor, 2, IndexedSeq(A, T, A, G)))

    stream1 ! "go"
    stream2 ! "go"

    val result = poll(processor)

    assert(result == "-ATAG\nTATCG")

    system.terminate()
  }

}
