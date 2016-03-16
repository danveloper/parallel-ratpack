package parallel

import ratpack.rx.RxRatpack
import rx.Observable
import rx.functions.Func1
import rx.functions.Func2
import toDoInParallel.ParallelThing

import java.util.concurrent.CyclicBarrier

class ParallelManager {

  ParallelThing thingA = new ParallelThing()
  ParallelThing thingB = new ParallelThing()
  ParallelThing thingC = new ParallelThing()
  ParallelThing thingD = new ParallelThing()
  ParallelThing thingE = new ParallelThing()

  Observable fetchValue() {
    return thingA.fetchValue().flatMap(this.&fetchDependencies as Func1)
  }

  Observable fetchDependencies(Object item) {
    def thingBOb = thingB.fetchValue()
    def thingCOb = thingC.fetchValue()
    def thingDOb = thingD.fetchValue()
    def thingEOb = thingE.fetchValue()
    return forkAndFlatten([thingBOb, thingCOb, thingDOb, thingEOb])
        .reduce([item], this.&aggregate as Func2)
  }

  Observable forkAndFlatten(List<Observable> observableList) {
    //CyclicBarrier barrier = new CyclicBarrier(observableList.size())
    Observable.from(observableList).flatMap {
      it
    }
    .compose(RxRatpack.&forkEach)
    //.doOnNext { value -> Exceptions.uncheck({}, { barrier.await() }) }
  }

  static List aggregate(List aggregate, String current) {
    return aggregate << current
  }
}

