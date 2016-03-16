import parallel.ParallelManager
import ratpack.rx.RxRatpack

import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

RxRatpack.initialize()

ratpack {
  bindings {
    bind ParallelManager
  }

  handlers {
    get { ParallelManager manager ->
      manager.fetchValue().promiseSingle().then {
        render json(it)
      }
    }

  }
}
