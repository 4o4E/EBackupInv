package top.e404.ebackupinv.config

import org.bukkit.configuration.file.YamlConfiguration

object Config : AbstractConfig("config.yml") {
    var duration = -1L
    var keep = -1
    var check = -1L
    override fun YamlConfiguration.onLoad() {
        duration = getLong("duration")
        keep = getInt("keep")
        check = getLong("check")
    }
}