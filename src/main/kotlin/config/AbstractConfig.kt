package top.e404.ebackupinv.config

import org.bukkit.command.CommandSender
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.scheduler.BukkitTask
import top.e404.ebackupinv.EBackupInv
import top.e404.ebackupinv.util.runTaskAsync
import top.e404.ebackupinv.util.runTaskLater
import top.e404.ebackupinv.util.sendOrElse
import top.e404.ebackupinv.util.warn

/**
 * 代表一个配置文件
 *
 * @property plugin 属于的插件
 * @property jarPath 文件在jar中的路径
 * @property path 文件路径
 */
@Suppress("UNUSED")
abstract class AbstractConfig(
    val path: String,
    val jarPath: String? = path,
    val clearBeforeSave: Boolean = false,
) {
    val plugin = EBackupInv.instance
    val file = plugin.dataFolder.resolve(path)
    lateinit var config: YamlConfiguration
    var task: BukkitTask? = null

    /**
     * 保存默认的配置文件
     *
     * @param sender 出现异常时的接收者
     * @since 1.0.0
     */
    fun saveDefault(sender: CommandSender?) {
        if (!file.exists()) file.runCatching {
            if (!parentFile.exists()) parentFile.mkdirs()
            if (isDirectory) {
                val s = "`${path}`是目录, 请手动删除或重命名"
                sender.sendOrElse(s) { warn(s) }
                return
            }
            if (jarPath == null) {
                createNewFile()
                return@runCatching
            }
            writeText(plugin.getResource(jarPath)!!.readBytes().decodeToString())
        }.onFailure {
            val s = "保存默认配置文件`${path}`时出现异常"
            sender.sendOrElse(s) { warn(s, it) }
        }
    }

    open fun YamlConfiguration.onLoad() {}

    /**
     * 从文件加载配置文件
     *
     * @param sender 出现异常时的通知接收者
     * @since 1.0.0
     */
    fun load(sender: CommandSender?) {
        saveDefault(sender)
        val nc = YamlConfiguration()
        try {
            nc.load(file)
        } catch (e: InvalidConfigurationException) {
            val s = "配置文件`${path}`格式错误, 请检查配置文件, 此文件内容将不会重载"
            sender.sendOrElse(s) { warn(s, e) }
            return
        } catch (t: Throwable) {
            val s = "加载配置文件`${path}`时出现异常, 此文件内容将不会重载"
            sender.sendOrElse(s) { warn(s, t) }
            return
        }
        nc.onLoad()
        config = nc
    }

    open fun YamlConfiguration.beforeSave() {}

    open fun YamlConfiguration.afterSave() {}

    /**
     * 计划一次保存任务
     */
    fun scheduleSave() {
        if (task != null) return
        task = runTaskLater(10 * 60 * 20) {
            task = null
            runTaskAsync { save(null) }
        }
    }

    /**
     * 立即保存配置到文件
     *
     * @param sender 出现异常时的通知接收者
     * @since 1.0.0
     */
    fun save(sender: CommandSender?) {
        if (clearBeforeSave) config = YamlConfiguration()
        try {
            config.apply {
                beforeSave()
                save(file)
                afterSave()
            }
        } catch (t: Throwable) {
            val s = "保存配置文件`${path}`时出现异常"
            sender.sendOrElse(s) { warn(s, t) }
        }
    }

    fun shutdown() {
        val t = task ?: return
        t.cancel()
        save(null)
    }
}