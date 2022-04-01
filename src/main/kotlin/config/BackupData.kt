package top.e404.ebackupinv.config

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import top.e404.ebackupinv.backup.BackupTaskManager.isUUID
import top.e404.ebackupinv.data.Backup
import top.e404.ebackupinv.data.Backup.Companion.toItemMap
import top.e404.ebackupinv.data.PlayerBackups
import top.e404.ebackupinv.data.PlayerBackups.Companion.getPlayerBackups
import top.e404.ebackupinv.util.info
import top.e404.ebackupinv.util.uuid
import java.text.SimpleDateFormat
import java.util.*

object BackupData : AbstractConfig("data.yml", clearBeforeSave = true) {
    private val sdf = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")

    // name, backup
    var data = mutableMapOf<String, PlayerBackups>()
    override fun YamlConfiguration.onLoad() {
        data = getKeys(false).associateWith {
            getPlayerBackups(it)
        }.toMutableMap()
    }

    override fun YamlConfiguration.beforeSave() {
        for ((name, backup) in data.entries) {
            set(name, backup.toConfig())
        }
    }

    fun getPlayerBackups(nameOrUuid: String) = if (nameOrUuid.isUUID()) getPlayerBackupsByUuid(nameOrUuid)
    else getPlayerBackupsByName(nameOrUuid)

    fun getPlayerBackupsByName(name: String) = data[name]
    fun getPlayerBackupsByUuid(uuid: String) = data.values.firstOrNull { it.uuid == uuid }
    fun getBackupByName(name: String, time: Long) = getPlayerBackupsByName(name)?.getBackup(time)
    fun getBackupBuUuid(uuid: String, time: Long) = getPlayerBackupsByUuid(uuid)?.getBackup(time)

    fun savePlayer(player: Player): Backup {
        val time = System.currentTimeMillis()
        val inv = player.inventory.toItemMap()
        val ec = player.enderChest.toItemMap()
        return Backup(time, inv, ec).also {
            data.getOrPut(player.name) {
                PlayerBackups(player.name, player.uuid(), time, mutableMapOf())
            }.data[time] = it
            info("自动保存玩家`${player.name}`的背包, ${it.info()}")
            BackupData.save(null)
        }
    }

    fun Long.asTime() = sdf.format(Date(this))
}