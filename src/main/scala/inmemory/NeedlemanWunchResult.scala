package inmemory

/**
  * Created by dkettlestrings on 5/24/17.
  */
case class NeedlemanWunchResult(scores: Array[Array[Int]], traceback: Array[Array[TracebackEntry]], report: String)
