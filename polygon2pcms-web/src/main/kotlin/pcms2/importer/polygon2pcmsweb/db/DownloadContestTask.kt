package pcms2.importer.polygon2pcmsweb.db

import converter.Converter
import converter.RecompileCheckerStrategy
import pcms2.deployer.Deployer
import pcms2.deployer.DeployerConfig
import pcms2.Challenge
import pcms2.Problem
import pcms2.importer.polygon2pcmsweb.AppConfig
import polygon.download.PackageDownloader
import polygon.download.PolygonPackageType
import tempfilemanager.TemporaryFileManager
import java.io.File
import java.io.PrintStream
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "download_tasks")
class DownloadContestTask(id: Long, logfile: File, status: TaskStatus,
                          val uid: String,
                          val language: String,
                          val contestType: String) : Task(id, logfile, status) {
    override fun process(config: AppConfig): Boolean {
        PrintStream(logfile, "utf-8").use { logger ->
            val fileManager = TemporaryFileManager()
            try {
                val downloader = PackageDownloader(config.polygonUsername, config.polygonPassword, logger)
                val deployer = Deployer(config.vfsRoot, config.webRoot, logger)
                val contest = downloader.downloadContestDescriptor(uid)
                val converter = Converter(RecompileCheckerStrategy.POINTS, config.languageProps, config.executableProps, logger)
                val problemIdPrefix = "auto"
                for (problem in contest.problems.values) {
                    val problemId = Problem.getProblemId(problemIdPrefix, problem.url, problem.shortName)
                    if (deployer.getVfsProblemRevision(problemId) == problem.revision) {
                        continue
                    }
                    val probDir = fileManager.createTemporaryDirectory("_problem")
                    val packageType = downloader.downloadProblemDirectory(problem.url, probDir, fileManager)
                    val pcmsProblem = converter.convertProblem(probDir, "auto", packageType == PolygonPackageType.STANDARD)
                    deployer.copyToVFS(pcmsProblem, DeployerConfig.ALL)
                }
                val challenge = Challenge(contest, problemIdPrefix, contestType, language)
                val challengeFile = fileManager.createTemporaryFile("_challenge", ".xml")
                challenge.print(challengeFile)
                deployer.deployChallengeXML(challengeFile, challenge.id, DeployerConfig.ALL)
                return true
            } catch (e: Throwable) {
                e.printStackTrace(logger)
                return false
            } finally {
                fileManager.removeAll()
            }
        }
    }
}