package pcms2.importer.polygon2pcmsweb.worker

interface DispatcherService {
    fun submit(f: () -> Unit)
}