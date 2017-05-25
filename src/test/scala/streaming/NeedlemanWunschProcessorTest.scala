package streaming

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import inmemory._
import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration._
import language.postfixOps

/**
  * Created by dkettlestrings on 5/24/17.
  */
class NeedlemanWunschProcessorTest extends FunSuite {

  implicit val timeout = Timeout(45 seconds)


  test("works for sequences of the same size with a diagonal trace") {

    val system = ActorSystem("NeedlemanWunschProcessorTest")
    val conf = NeedlemanWunchConfig(matching = 5, mismatch = -1, gap = -2)

    val processor = system.actorOf(Props(classOf[NeedlemanWunschProcessor], conf))

    val stream1 = system.actorOf(Props(classOf[FixedSequenceGenerator], processor, 1, IndexedSeq(T, C, A, T, A)))
    val stream2 = system.actorOf(Props(classOf[FixedSequenceGenerator], processor, 2, IndexedSeq(T, C, C, T, A)))

    stream1 ! "go"
    stream2 ! "go"

    def poll(): NeedlemanWunchResult = {

      val isActive = Await.result(processor ? "isActive", Duration(5, "seconds")).asInstanceOf[Boolean]
      if(isActive) {Thread.sleep(5000);poll()} else Await.result(processor ? "result", Duration(45, "seconds")).asInstanceOf[NeedlemanWunchResult]
    }

    val result = poll()

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

    system.terminate()

  }

}
