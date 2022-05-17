package top.e404.ebackupinv.listener

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import top.e404.ebackupinv.EBackupInv
import top.e404.ebackupinv.config.Config
import top.e404.ebackupinv.backup.PlayerBackupManager.saveAndStop
import top.e404.ebackupinv.backup.PlayerBackupManager.scheduleBackup
import top.e404.ebackupinv.backup.PlayerBackupManager.triggerBackup

object EListener : Listener {
    fun register() {
        Bukkit.getPluginManager().registerEvents(this, EBackupInv.instance)
    }

    @EventHandler
    fun PlayerJoinEvent.onJoin() {
        player.scheduleBackup()
    }

    @EventHandler
    fun PlayerQuitEvent.onQuit() {
        player.saveAndStop()
    }

    @EventHandler
    fun PlayerDeathEvent.onDeath() {
        if (Config.onDeath) entity.triggerBackup()
    }
}