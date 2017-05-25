# dna_alignment

This README exists to help you build and run the tests as well as answer questions.

## Build and run tests

This is a simple `sbt` project.  To build and run the tests execute `sbt test`.  Note that the performance tests are currently `@Ignore`d to save time and since they don't assert anything.  If you want to run them, just remove the annotations.

## Time spent

Despite the fact that I am not satisfied with the current results, I wanted to cut myself off after 12 hours.  I'll get into more details below, but I wanted to take the opportunity to explain why I did not ask for more help.

The reason I did not ask for more help is that I don't feel confused about the problem.  Except for the minor questions I asked, I always felt that I understood the problem, had a solution in mind, and was making progress (albeit slowly).  The only "help" I needed may have been hints to speed me up, which I did not want to get.

## Questions

### What, if anything, did you learn from doing this exercise?

1. I certainly learned about that algorithm.  I made sure to spend time thinking about it and making sure I had a decent grasp of it before I started writing too much code.  It's pretty impressive and I'm going to do a little research on it now that the time crunch is over.  It reminds me of some work I was doing with Lincoln Laboratory years ago.  This was before I knew how to program, but the traceback approach and the arrow diagrams really make me wonder if this would be a good approach for the problem they were solving.

2. I learned about dynamic programming a little bit.  Since I don't have a CS degree, I have only heard of it a few times.  Given my struggles implementing the memory-optimized streaming implementation, it looks like I've found a weakness in my skill-set.  I plan on learning some this weekend.

3. I learned once again that I hate mutable state, but that sometimes it's required.


###  If you did it over again from scratch, what sorts of things might you want to differently, or better?

1. The first thing would be to start using some better identifiers or helper functions to make the code more readable.  There are so many `i`s and such flying around that it's hard to follow.

2. Find a way to decouple the guts of the algorithm from the state changes.  The optimized streaming approach requires a lot of state change.  Unfortunately, those changes are littered throughout the algorithm logic which makes debugging issues much more difficult.  One way would be to use something like an Akka Finite State Machine.

3. I would abandon any hopes of a clean, functional programming approach.  While I did end up abandoning it for the most part, I wasted a little time trying to write it that way from scratch.


### Where could you take this tiny app from here? What might make it more practical, or even useful?

1. The most obvious thing to do would be to make the optimized streaming implementation actually optimal.  It works, but I didn't have time to track down the root cause.  Get it working first, then optimize!

2. Instead of returning a `String`, the algorithm should return some type of structured data that represents substitutions, deletions, etc.  It would allow clients to choose their own "interpreter" for the results.  I had this when I was chasing my tail with the functional approach, but it got trashed with the change of approach.  

3. Having a cluster of processors up and ready to work in parallel would be nice.

4. Allowing the processor to exert backpressure would be a good idea.  It would be very easy for the actor to fall behind and start dropping messages once the sequence gets large.  See Akka Streaming.

5. Turning this into a service would be a good idea, especially if it implemented persistence.  Clients love it when you save their results for them.

6. Implementing any of the generalizations found on the Wikipedia page.






