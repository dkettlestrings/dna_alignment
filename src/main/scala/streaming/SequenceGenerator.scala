package streaming

import akka.actor.{Actor, ActorRef}
import inmemory.{DNASequence, Nucleotide}

import scala.util.Random

/**
  * Created by dkettlestrings on 5/24/17.
*/

trait SequenceGenerator extends Actor {

  val nwProcessor: ActorRef

  val minWait = 100
  val maxWait = 2000

  val inputChannel: Int

  protected def holdUp(): Unit = {

    val waitTime = minWait + Random.nextInt(maxWait - minWait + 1)
    Thread.sleep(waitTime)
  }

}

class RandomSequenceGenerator(processor: ActorRef, channel: Int) extends SequenceGenerator {

  override val nwProcessor = processor

  override val inputChannel = channel

  override def receive = {

    case n: Int =>
      (1 to n).foreach(_ => {nwProcessor ! (inputChannel, Nucleotide.next()); holdUp()})
      nwProcessor ! (inputChannel, "close")


  }

}

class FixedSequenceGenerator(processor: ActorRef, channel: Int, sequence: DNASequence) extends SequenceGenerator {

  override val nwProcessor = processor

  override val inputChannel = channel

  override def receive = {

    case "go" =>
      sequence.foreach(n => {nwProcessor ! (inputChannel, n); holdUp()})
      nwProcessor ! (inputChannel, "close")


  }

}
