package top.e404.ebackupinv.listener

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import top.e404.ebackupinv.EBackupInv

interface EListener : Listener {
    fun register() {
        Bukkit.getPluginManager().registerEvents(this, EBackupInv.instance)
    }
}