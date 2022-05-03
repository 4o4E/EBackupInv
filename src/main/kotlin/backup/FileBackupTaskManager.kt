package top.e404.ebackupinv.backup

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask
import top.e404.ebackupinv.EBackupInv
import top.e404.ebackupinv.config.Config
import top.e404.ebackupinv.util.debug
import top.e404.ebackupinv.util.info
import top.e404.ebackupinv.util.warn
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object FileBackupTaskManager {
    private val scheduler = Bukkit.getScheduler()
    private var task: BukkitTask? = null
    private val pluginPath = EBackupInv.instance.dataFolder.absolutePath
    private val serverPath = File("").absolutePath

    fun schedule() {
        task?.cancel()
        if (!Config.fileEnable) return
        val duration = Config.fileDuration * 60 * 20
        info("计划保存任务, 保存间隔${Config.fileDuration}分钟")
        task = scheduler.runTaskTimerAsynchronously(
            EBackupInv.instance,
            Runnable {
                try {
                    backup()
                } catch (t: Throwable) {
                    warn("保存时出现异常", t)
                }
            },
            duration,
            duration
        )
    }

    fun backup() {
        val backupDir = File(
            Config.filePath.placeholders("plugin" to pluginPath, "server" to serverPath)
        ).apply { mkdirs() }
        val time = SimpleDateFormat(Config.fileTime).format(Date())
        val zip = backupDir.resolve(
            Config.fileName.placeholders("time" to time)
        )
        val files = Config.files.map {
            File(it.placeholders("plugin" to pluginPath, "server" to serverPath))
        }
        if (files.isEmpty()) return
        val zipPath = zip.absolutePath
        info("开始保存文件$zipPath")
        zip.outputStream().use { fos ->
            fos.buffered().use { bos ->
                ZipOutputStream(bos).use { zos ->
                    for (dir in files) if (dir.exists()) {
                        val name = dir.name
                        debug("保存${name}中")
                        backup(zos, dir, name)
                        debug("保存${name}完成")
                    }
                }
            }
        }
        info("完成保存文件$zipPath")
        clean(backupDir)
    }

    fun backup(zos: ZipOutputStream, file: File, path: String) {
        if (file.isDirectory) {
            val files = file.listFiles() ?: return
            for (f in files) backup(zos, f, "$path/${file.name}")
            return
        }
        zos.putNextEntry(ZipEntry("$path/${file.name}"))
        zos.write(file.readBytes())
    }

    fun clean(dir: File) {
        val files = dir.listFiles() ?: return
        val filter = files.filter { Config.fileRegex.matches(it.name) }
        if (files.size < Config.fileRetain) return
        val sorted = filter.map {
            val date = it.name.removeSuffix(".zip")
            SimpleDateFormat(Config.fileTime).parse(date).time to it
        }.sortedBy {
            it.first
        }.toMutableList()
        repeat(Config.fileRetain) {
            sorted.removeLastOrNull()
        }
        sorted.forEach { (_, f) ->
            debug("清理过期文件: ${f.name}")
            f.delete()
        }
    }

    private fun String.placeholders(vararg placeholder: Pair<String, String>): String {
        var s = this
        for ((k, v) in placeholder) s = s.replace("{$k}", v)
        return s
    }
}