package top.e404.ebackupinv.backup

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

/**
 * 代表玩家背包的一次备份(一个文件)
 *
 * @property uuid 玩家uuid
 * @property time 备份时间戳
 * @property inv 背包物品
 * @property ec 末影箱物品
 */
data class PlayerBackup(
    val uuid: String,
    val time: Long,
    val inv: MutableMap<Int, ItemStack>,
    val ec: MutableMap<Int, ItemStack>,
) {
    companion object {
        fun ConfigurationSection.getBackup(path: String, uuid: String) =
            getConfigurationSection(path)!!.let { config ->
                PlayerBackup(
                    uuid,
                    config.getLong("time"),
                    config.getItemMap("inventory"),
                    config.getItemMap("enderChest")
                )
            }

        fun ConfigurationSection.getItemMap(path: String) =
            getConfigurationSection(path)!!.let { config ->
                config.getKeys(false).associate { key ->
                    key.toInt() to config.getItemStack(key)!!
                }.toMutableMap()
            }

        fun Inventory.toItemMap() = mutableMapOf<Int, ItemStack>().apply {
            for (i in 0 until this@toItemMap.size) {
                val item = getItem(i) ?: continue
                if (item.type == Material.AIR) continue
                put(i, item)
            }
        }

        fun Map<Int, ItemStack>.toConfig() = YamlConfiguration().apply {
            for ((index, item) in entries) set(index.toString(), item)
        }

        private val invRange = 9..54

        fun Map<Int, ItemStack>.toInventory(title: String) =
            keys.maxOfOrNull { it }
                .let { if (it == null) 9 else ceil(it / 9.0).toInt() * 9 }
                .coerceIn(invRange)
                .let { Bukkit.createInventory(null, it, title) }
                .also { for ((index, item) in entries) it.setItem(index, item) }

        fun fromFile(f: File, uuid: String) = YamlConfiguration.loadConfiguration(f).getBackup("backup", uuid)

        private val sdf = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
        fun Long.formatAsTimeStamp() = sdf.format(Date(this))!!
    }

    fun save() = YamlConfiguration().apply {
        set("backup", toConfig())
        save(PlayerBackupManager.backupDir.resolve(uuid).resolve("$time.yml"))
    }

    fun toConfig() = YamlConfiguration().apply {
        set("time", time)
        set("inventory", inv.toConfig())
        set("enderChest", ec.toConfig())
    }
}