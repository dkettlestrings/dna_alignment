package common

/**
  * Created by dkettlestrings on 5/24/17.
  */
sealed trait TracebackEntry

object Left extends TracebackEntry
object Up extends TracebackEntry
object Diagonal extends TracebackEntry
object Done extends TracebackEntry
