package top.e404.ebackupinv.data

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import top.e404.ebackupinv.config.BackupData.asTime
import top.e404.ebackupinv.data.Backup.Companion.getBackup
import java.util.concurrent.ConcurrentHashMap

/**
 * 代表一个玩家的所有备份
 *
 * @property name 玩家名字
 * @property uuid 玩家uuid
 * @property last 最后一次备份时间
 * @property data 玩家备份数据
 */
data class PlayerBackups(
    val name: String,
    val uuid: String,
    val last: Long,
    val data: ConcurrentHashMap<Long, Backup>,
) {
    companion object {
        fun ConfigurationSection.getPlayerBackups(path: String) =
            getConfigurationSection(path)!!.let { config ->
                PlayerBackups(
                    config.getString("name")!!,
                    config.getString("uuid")!!,
                    config.getLong("last"),
                    config.getPlayerBackupsMap("data")
                )
            }

        fun ConfigurationSection.getPlayerBackupsMap(path: String): ConcurrentHashMap<Long, Backup> =
            getConfigurationSection(path)!!.let { config ->
                ConcurrentHashMap(config.getKeys(false).associate { key ->
                    key.toLong() to config.getBackup(key)
                })
            }

        fun MutableMap<Long, Backup>.toConfig() =
            YamlConfiguration().apply {
                for ((time, backup) in entries) set(time.toString(), backup.toConfig())
            }
    }

    fun toConfig() =
        YamlConfiguration().apply {
            set("name", this@PlayerBackups.name)
            set("uuid", uuid)
            set("last", last)
            set("data", data.toConfig())
        }

    fun getBackup(time: Long) = data[time]

    fun info() = """玩家${name}($uuid), 备份数${data.size}, 最近一次备份于${last.asTime()}"""
    fun list() = data.values.map { it.info() }

    /**
     * 清理过时备份
     *
     * @param t 时间戳, 此时间点之前的备份都会被清理
     * @return 清理数量
     */
    fun cleanTimeout(t: Long): Int {
        var times = 0
        for (key in data.keys.toList()) if (key < t) {
            data.remove(key)
            times++
        }
        return times
    }
}