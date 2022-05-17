package top.e404.ebackupinv.translate

import org.bukkit.configuration.file.YamlConfiguration
import top.e404.ebackupinv.EBackupInv
import top.e404.ebackupinv.backup.PlayerBackup.Companion.getBackup
import top.e404.ebackupinv.backup.PlayerBackupManager
import top.e404.ebackupinv.util.info
import top.e404.ebackupinv.util.runTaskAsync
import top.e404.ebackupinv.util.warn

object Translate1To2 {
    fun translate() {
        val file = EBackupInv.instance.dataFolder.resolve("data.yml")
        if (!file.exists()) return
        info("&b开始从旧版本导入备份数据")
        val cfg = try {
            YamlConfiguration().apply { load(file) }
        } catch (t: Throwable) {
            info("&b错误的旧版本数据, 停止导入")
            return
        }
        var save = false
        val keys = cfg.getKeys(false)
        if (keys.isEmpty()) {
            info("&b无效的旧版本数据, 停止导入")
            return
        }
        for (playerName in keys) try {
            val playerCfg = cfg.getConfigurationSection(playerName)!!
            val uuid = playerCfg.getString("uuid")!!
            val data = playerCfg.getConfigurationSection("data")!!
            for (time in data.getKeys(false)) try {
                val backupCfg = data.getBackup(time, uuid)
                val i = PlayerBackupManager.index.firstOrNull { it.uuid == uuid }
                    ?: PlayerBackupManager.BackupIndex(uuid, playerName, mutableListOf())
                        .also { PlayerBackupManager.index.add(it) }
                i.backups.add(backupCfg.time)
                save = true
                runTaskAsync { backupCfg.save() }
            } catch (t: Throwable) {
                warn("从旧版本配置文件中导入时玩家${playerName}的备份${time}时出现异常, 已跳过", t)
            }
        } catch (t: Throwable) {
            warn("从旧版本配置文件中导入时玩家${playerName}数据时出现异常, 已跳过", t)
        }
        info("&b完成从旧版本导入备份数据")
        if (save) PlayerBackupManager.scheduleSave()
    }
}