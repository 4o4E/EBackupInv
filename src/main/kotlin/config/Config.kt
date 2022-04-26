package top.e404.ebackupinv.config

import org.bukkit.configuration.file.YamlConfiguration

object Config : AbstractConfig("config.yml") {
    var debug = false
    var update = true
    var prefix = "&7[&aEBackupInv&7]"
    var duration = 216000L
    var keep = 72
    var check = 3L
    var onDeath = false
    var cooldown = 600L
    override fun YamlConfiguration.onLoad() {
        debug = getBoolean("debug")
        update = getBoolean("update", true)
        prefix = getString("prefix") ?: "&7[&aEBackupInv&7]"
        duration = getLong("duration")
        keep = getInt("keep")
        check = getLong("check")
        onDeath = getBoolean("on_death")
        cooldown = getLong("cooldown")
    }
}