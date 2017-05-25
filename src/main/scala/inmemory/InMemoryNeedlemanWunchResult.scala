package inmemory

import common.TracebackEntry

/**
  * Created by dkettlestrings on 5/24/17.
  */
case class InMemoryNeedlemanWunchResult(scores: Array[Array[Int]], traceback: Array[Array[TracebackEntry]], report: String)
