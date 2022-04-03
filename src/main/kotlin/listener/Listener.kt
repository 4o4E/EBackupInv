package top.e404.ebackupinv.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import top.e404.ebackupinv.backup.BackupTaskManager
import top.e404.ebackupinv.config.BackupData
import top.e404.ebackupinv.config.Config

object Listener : EListener {
    @EventHandler
    fun PlayerJoinEvent.onJoin() = BackupTaskManager.onPlayerJoin(player)

    @EventHandler
    fun PlayerQuitEvent.onQuit() = BackupTaskManager.onPlayerQuit(player)

    @EventHandler
    fun PlayerDeathEvent.onDeath() = apply {
        if (Config.onDeath) BackupData.savePlayer(entity)
    }
}