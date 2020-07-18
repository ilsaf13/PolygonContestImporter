package pcms2.importer.polygon2pcmsweb.db

import pcms2.importer.polygon2pcmsweb.AppConfig
import java.io.File
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tasks")
abstract class Task(@Id @GeneratedValue(strategy = GenerationType.AUTO) open val id: Long = 0,
                    open val logfile: File,
                    open var status: TaskStatus) {
    abstract fun process(config: AppConfig): Boolean
}