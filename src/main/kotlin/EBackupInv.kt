package top.e404.ebackupinv

import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import top.e404.ebackupinv.backup.FileBackupTaskManager
import top.e404.ebackupinv.command.CommandManager
import top.e404.ebackupinv.config.Config
import top.e404.ebackupinv.listener.EListener
import top.e404.ebackupinv.backup.PlayerBackupManager
import top.e404.ebackupinv.backup.PlayerBackupManager.scheduleBackup
import top.e404.ebackupinv.update.Update
import top.e404.ebackupinv.util.info
import top.e404.ebackupinv.util.runTaskTimerAsync

class EBackupInv : JavaPlugin() {
    companion object {
        lateinit var instance: EBackupInv
        val logo = listOf(
            """&6 ______     ______     ______     ______     __  __     __  __     ______ """,
            """&6/\  ___\   /\  == \   /\  __ \   /\  ___\   /\ \/ /    /\ \/\ \   /\  == \""",
            """&6\ \  __\   \ \  __<   \ \  __ \  \ \ \____  \ \  _"-.  \ \ \_\ \  \ \  _-/""",
            """&6 \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\  """,
            """&6  \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_____/   \/_/  """
        )
    }

    override fun onEnable() {
        instance = this
        Metrics(this, 14814)
        Config.load(null)
        //BackupData.load(null)
        PlayerBackupManager.load(null)
        CommandManager.register("ebackupinv")
        EListener.register()
        val d = Config.check * 60 * 60 * 20
        runTaskTimerAsync(d, d, PlayerBackupManager::cleanTimeout)
        Bukkit.getOnlinePlayers().forEach { it.scheduleBackup() }
        Update.init()
        FileBackupTaskManager.schedule()
        for (line in logo) info(line)
        info("&a加载完成")
    }

    override fun onDisable() {
        PlayerBackupManager.shutdown()
        Bukkit.getScheduler().cancelTasks(this)
    }
}