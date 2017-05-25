package inmemory

import System.currentTimeMillis

/**
  *
  * My go-to simple method for profiling at the mathod/codeblock level.
  * I originally found it here: https://stackoverflow.com/questions/9160001/how-to-profile-methods-in-scala
  *
  * Created by dkettlestrings on 5/24/17.
  */
object Profiler {

  def profile[R](code: => R, t: Long = currentTimeMillis) = (code, currentTimeMillis - t)

}
