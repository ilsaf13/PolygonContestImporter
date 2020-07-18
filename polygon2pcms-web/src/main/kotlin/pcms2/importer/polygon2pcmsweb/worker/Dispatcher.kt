package pcms2.importer.polygon2pcmsweb.worker

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pcms2.importer.polygon2pcmsweb.AppConfig
import java.util.concurrent.Executors

@Service
class Dispatcher @Autowired constructor(config: AppConfig): DispatcherService {
    private val executor = Executors.newFixedThreadPool(config.workersCount)

    override fun submit(f: () -> Unit) {
        executor.execute(f)
    }
}