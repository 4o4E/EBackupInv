package top.e404.ebackupinv.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import top.e404.ebackupinv.backup.BackupTaskManager

object Listener : EListener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) = event.apply {
        BackupTaskManager.onPlayerJoin(player)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) = event.apply {
        BackupTaskManager.onPlayerQuit(player)
    }
}