import parallel.ParallelManager
import ratpack.rx.RxRatpack
import ratpack.server.Service
import ratpack.server.StartEvent

import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

ratpack {
  bindings {
    bind ParallelManager
    bindInstance Service, new Service() {
      @Override
      void onStart(StartEvent event) throws Exception {
        RxRatpack.initialize()
      }
    }

  }

  handlers {
    get { ParallelManager manager ->
      manager.fetchValue().bindExec().subscribe {
        render json(it)
      }
    }

  }
}
