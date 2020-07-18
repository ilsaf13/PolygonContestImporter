package pcms2.importer.polygon2pcmsweb.db

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskDAO : CrudRepository<Task, Long> {
    fun findByStatus(status: TaskStatus): List<Task>
}