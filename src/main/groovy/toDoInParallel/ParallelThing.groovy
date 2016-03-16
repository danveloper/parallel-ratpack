package toDoInParallel

import rx.Observable

class ParallelThing {
  Observable fetchValue() {
    return Observable.just("this will be mocked and you are never to actually get here")
  }
}
