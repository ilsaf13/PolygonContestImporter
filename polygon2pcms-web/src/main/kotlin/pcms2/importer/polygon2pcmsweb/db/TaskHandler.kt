package pcms2.importer.polygon2pcmsweb.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import pcms2.importer.polygon2pcmsweb.AppConfig
import pcms2.importer.polygon2pcmsweb.worker.DispatcherService

@EnableScheduling
@Component
class TaskHandler @Autowired constructor(private val config: AppConfig,
                                         private val dispatcher: DispatcherService,
                                         private val taskRepository: TaskDAO) {
    @Scheduled(fixedDelay = 5000)
    fun processTask() {
        val tasks = taskRepository.findByStatus(TaskStatus.QUEUED)
        if (tasks.isEmpty()) {
            return
        }
        val task = tasks[0]
        task.status = TaskStatus.IN_PROGRESS
        taskRepository.save(task)
        val success = task.process(config)
        task.status = when (success) {
            true -> TaskStatus.FINISHED
            else -> TaskStatus.FAILED
        }
        taskRepository.save(task)
    }
}