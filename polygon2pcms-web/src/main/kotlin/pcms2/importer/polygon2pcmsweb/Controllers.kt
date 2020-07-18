package pcms2.importer.polygon2pcmsweb

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import pcms2.importer.polygon2pcmsweb.db.DownloadContestTask
import pcms2.importer.polygon2pcmsweb.db.Task
import pcms2.importer.polygon2pcmsweb.db.TaskDAO
import pcms2.importer.polygon2pcmsweb.db.TaskStatus
import java.io.File
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*

@Controller
class Controllers @Autowired constructor(private val config: AppConfig, private val taskRepository: TaskDAO) {

    @GetMapping("/")
    fun home() = "redirect:/import"

    @GetMapping("/hello")
    fun hello() = "hello"

    @GetMapping("/import")
    fun import(model: Model) = "import"

    @PostMapping("/import")
    fun doImport(model: Model, @RequestParam uid: String, @RequestParam language: String, @RequestParam contestType: String): String {
        val date = SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(Date())
        val logfile = File(config.logsDirectory, "${date}.log")
        if (!logfile.parentFile.exists()) {
            logfile.parentFile.mkdirs()
        }
        PrintWriter(logfile).use {
            it.println("${logfile} queued")
        }
        val task = DownloadContestTask(0, logfile, TaskStatus.QUEUED, uid, language, contestType)
        taskRepository.save(task)
        model.addAttribute("createdID", task.id)
        return "import"
    }

    @GetMapping("/tasks")
    fun tasks(model: Model): String {
        model.addAttribute("tasks", taskRepository.findAll().sortedByDescending { it.id })
        return "tasks"
    }

    @GetMapping("/showlog/{logfile}")
    fun showlog(model: Model, @PathVariable("logfile") filePath: String) : String {
        val code = File(config.logsDirectory, filePath).readLines().joinToString(separator = "\n")
        model.addAttribute("code", code)
        return "show"
    }
}
