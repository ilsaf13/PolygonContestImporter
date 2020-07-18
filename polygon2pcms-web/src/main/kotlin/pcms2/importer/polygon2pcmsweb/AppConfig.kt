package pcms2.importer.polygon2pcmsweb

import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.util.*

@Service
class AppConfig {
    final val webRoot: File
    final val vfsRoot: File
    final val importerHome: File
    final val logsDirectory: File
    final val polygonUsername: String
    final val polygonPassword: String
    final val workersCount: Int
    final val languageProps: Properties
    final val executableProps: Properties
    final val datasourceUrl: String
    final val datasourceUsername: String
    final val datasourcePassword: String
    companion object {
        const val configFilename = "webimporter.properties"
        const val logDirname = "logs"
        const val importerHomeVarname = "IMPORTER_HOME"
        const val languagePropertiesFilename = "language.properties"
        const val executablePropertiesFilename = "executable.properties"
    }

    init {
        val importerHomeProperty = System.getenv(importerHomeVarname) ?: throw RuntimeException("${importerHomeVarname} variable not set")
        importerHome = File(importerHomeProperty)
        if (!importerHome.exists()) {
            throw RuntimeException("importer home not found: ${importerHome.path}")
        }
        logsDirectory = File(importerHome, logDirname)
        val configFile = File(importerHome, configFilename)
        if (!configFile.exists() || !configFile.canRead()) {
            throw RuntimeException("importer properties can't be read: ${configFile.path}")
        }
        val props = readProperties(configFile)
        webRoot = props.getProperty("web")?.let { File(it) } ?: throw RuntimeException("'web' is not set in properties file: ${configFile.path}")
        vfsRoot = props.getProperty("vfs")?.let { File(it) } ?: throw RuntimeException("'vfs' is not set in properties file: ${configFile.path}")
        if (!logsDirectory.exists()) {
            if (!logsDirectory.mkdir()) {
                throw RuntimeException("Couldn't create log directory ${logsDirectory.path}")
            }
        }
        polygonUsername = getPropertyValue(props, "polygonUsername")
        polygonPassword = getPropertyValue(props, "polygonPassword")
        workersCount = props.getProperty("workersCount", "1").toInt()
        languageProps = readProperties(File(importerHome, languagePropertiesFilename))
        executableProps = readProperties(File(importerHome, executablePropertiesFilename))
        datasourceUrl = getPropertyValue(props, "datasource.url")
        datasourceUsername = getPropertyValue(props, "datasource.username")
        datasourcePassword = getPropertyValue(props, "datasource.password")
    }

    private fun getPropertyValue(props: Properties, name: String) =
            props.getProperty(name) ?: throw RuntimeException("'${name}' is not set")

    private fun readProperties(configFile: File) = Properties().apply {
        if (configFile.exists()) {
            InputStreamReader(FileInputStream(configFile), "utf-8").use {
                load(it)
            }
        }
    }
}
