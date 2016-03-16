package parallel

import ratpack.rx.RxRatpack
import ratpack.test.exec.ExecHarness
import rx.Observable
import spock.lang.AutoCleanup
import spock.lang.Specification
import toDoInParallel.ParallelThing

class ParallelManagerSpec extends Specification {

  @AutoCleanup
  ExecHarness harness = ExecHarness.harness()

  ParallelManager manager

  ParallelThing parallelThingUno
  ParallelThing parallelThingDos
  ParallelThing parallelThingTres
  ParallelThing parallelThingQuattro
  ParallelThing parallelThingCinco


  def setup() {
    parallelThingUno = Mock(ParallelThing)
    parallelThingDos = Mock(ParallelThing)
    parallelThingTres = Mock(ParallelThing)
    parallelThingQuattro = Mock(ParallelThing)
    parallelThingCinco = Mock(ParallelThing)
    manager = new ParallelManager(thingA: parallelThingUno,
        thingB: parallelThingDos,
        thingC: parallelThingTres,
        thingD: parallelThingQuattro,
        thingE: parallelThingCinco)
    RxRatpack.initialize()
  }

  def 'fetches item'() {
    //You may have to run this over and over again to get it to fail - it's a close race!
    when:
    List<String> result = harness.yield { manager.fetchValue().promiseSingle() }.valueOrThrow

    then:
    1 * parallelThingUno.fetchValue() >> Observable.just("mocked uno")
    1 * parallelThingDos.fetchValue() >> Observable.just("mocked dos")
    1 * parallelThingTres.fetchValue() >> Observable.just("mocked tres")
    1 * parallelThingQuattro.fetchValue() >> Observable.just("mocked quattro")
    1 * parallelThingCinco.fetchValue() >> Observable.just("mocked cinco")

    result.each {
      println it
    }
    result.size() == 5

  }

}
