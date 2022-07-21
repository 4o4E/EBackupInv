package top.e404.ebackupinv.backup

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import top.e404.ebackupinv.EBackupInv
import top.e404.ebackupinv.config.AbstractConfig
import top.e404.ebackupinv.config.Config
import top.e404.ebackupinv.backup.PlayerBackup.Companion.toItemMap
import top.e404.ebackupinv.translate.Translate1To2
import top.e404.ebackupinv.util.*
import java.util.concurrent.CopyOnWriteArrayList

object PlayerBackupManager : AbstractConfig("index.yml", null, true) {
    val backupDir = EBackupInv.instance.dataFolder.resolve("backup")

    // name, index
    val index = CopyOnWriteArrayList<BackupIndex>()
    private val tasks = mutableMapOf<Player, BukkitTask>()

    operator fun get(name: String) = index.firstOrNull{ it.name.equals(name, true) }

    override fun YamlConfiguration.onLoad() {
        index.clear()
        for (uuid in getKeys(false)) {
            val cs = getConfigurationSection(uuid) ?: continue
            index.add(BackupIndex(uuid, cs.getString("name")!!, cs.getLongList("backups")))
        }
        if (index.isEmpty()) runTaskAsync {
            Translate1To2.translate()
        }
    }

    override fun YamlConfiguration.beforeSave() {
        index.forEach {
            val c = YamlConfiguration()
            c.set("name", it.name)
            c.set("backups", it.backups)
            set(it.uuid, c)
        }
    }

    /**
     * 创建一次备份
     */
    fun Player.backup() {
        val time = System.currentTimeMillis()
        val inv = inventory.toItemMap()
        val ec = enderChest.toItemMap()
        if (inv.isEmpty() && ec.isEmpty()) return
        runTaskAsync {
            val uuid = uuid()
            PlayerBackup(uuid, time, inv, ec).let {
                val i = get(name)
                    ?: BackupIndex(uuid, name, mutableListOf())
                        .also { i -> index.add(i) }
                i.backups.add(it.time)
                it.save() // 保存文件
                scheduleSave() // 计划保存索引
            }
        }
    }

    /**
     * 开始计划备份
     */
    fun Player.scheduleBackup() {
        tasks[this]?.cancel()
        tasks[this] = runTaskTimer(Config.duration, Config.duration) { backup() }
    }

    /**
     * 计划外的备份, 备份后重新计划备份
     */
    fun Player.triggerBackup() {
        backup()
        scheduleBackup()
    }

    fun Player.saveAndStop() {
        tasks[this]?.cancel()
        backup()
    }

    /**
     * 清理过期备份
     */
    fun cleanTimeout() {
        val timeout = System.currentTimeMillis() - Config.keep * 60 * 60 * 1000
        index.forEach { it.cleanTimeout(timeout) }
        index.removeIf { it.backups.isEmpty() }
    }

    data class BackupIndex(
        val uuid: String,
        val name: String,
        val backups: MutableList<Long>
    ) {
        val playerDir by lazy { backupDir.resolve(uuid) }

        fun getByStamp(stamp: Long): PlayerBackup? {
            val file = playerDir.resolve("$stamp.yml")
            if (!file.exists()) return null
            return PlayerBackup.fromFile(file, uuid)
        }

        fun cleanTimeout(timeout: Long) {
            val timeoutBackups = backups.filter { it < timeout }
            for (stamp in timeoutBackups) {
                backups.remove(stamp)
                playerDir.resolve("$stamp.yml").delete()
            }
            debug("自动清理玩家${name}过期备份共${timeoutBackups.size}个")
        }
    }
}