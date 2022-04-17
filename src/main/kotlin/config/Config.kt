package top.e404.ebackupinv.config

import org.bukkit.configuration.file.YamlConfiguration

object Config : AbstractConfig("config.yml") {
    var debug = false
    var duration = -1L
    var keep = -1
    var check = -1L
    var onDeath = false
    override fun YamlConfiguration.onLoad() {
        debug = getBoolean("debug")
        duration = getLong("duration")
        keep = getInt("keep")
        check = getLong("check")
        onDeath = getBoolean("on_death")
    }
}