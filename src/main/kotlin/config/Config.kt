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
    var fileEnable = false
    var fileDuration = 10L
    var filePath = "{plugin}/backup"
    var fileTime = "yyyy.MM.dd_HH.mm.ss"
    var fileRetain = 10
    var fileName = "{time}.zip"
    var fileRegex = Regex("\\d{4}\\.\\d{2}\\.\\d{2}_\\d{2}\\.\\d{2}\\.\\d{2}\\.zip")
    var files = mutableListOf<String>()
    override fun YamlConfiguration.onLoad() {
        debug = getBoolean("debug")
        update = getBoolean("update", true)
        prefix = getString("prefix") ?: "&7[&aEBackupInv&7]"
        duration = getLong("duration")
        keep = getInt("keep")
        check = getLong("check")
        onDeath = getBoolean("on_death")
        cooldown = getLong("cooldown")
        fileEnable = getBoolean("file_enable")
        fileDuration = getLong("file_duration")
        filePath = getString("file_path") ?: "{plugin}/backup"
        fileTime = getString("file_time") ?: "yyyy.MM.dd_HH.mm.ss"
        fileRetain = getInt("file_retain")
        fileName = getString("file_name") ?: "{time}.zip"
        fileRegex = Regex(getString("file_regex") ?: "\\d{4}\\.\\d{2}\\.\\d{2}_\\d{2}\\.\\d{2}\\.\\d{2}\\.zip")
        files = getStringList("files")
    }
}