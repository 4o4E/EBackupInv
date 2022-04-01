package top.e404.ebackupinv

import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import top.e404.ebackupinv.backup.BackupTaskManager
import top.e404.ebackupinv.command.CommandManager
import top.e404.ebackupinv.config.BackupData
import top.e404.ebackupinv.config.Config
import top.e404.ebackupinv.listener.Listener
import top.e404.ebackupinv.update.Update
import top.e404.ebackupinv.util.color
import top.e404.ebackupinv.util.info
import top.e404.ebackupinv.util.runTaskTimer

class EBackupInv : JavaPlugin() {
    companion object {
        val logo = listOf(
            """&6 ______     ______     ______     ______     __  __     __  __     ______   __     __   __     __   __  """.color(),
            """&6/\  ___\   /\  == \   /\  __ \   /\  ___\   /\ \/ /    /\ \/\ \   /\  == \ /\ \   /\ "-.\ \   /\ \ / /  """.color(),
            """&6\ \  __\   \ \  __<   \ \  __ \  \ \ \____  \ \  _"-.  \ \ \_\ \  \ \  _-/ \ \ \  \ \ \-.  \  \ \ \'/   """.color(),
            """&6 \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\    \ \_\  \ \_\\"\_\  \ \__|   """.color(),
            """&6  \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_____/   \/_/     \/_/   \/_/ \/_/   \/_/    """.color())
        const val prefix = "EBackupInv"
        lateinit var instance: EBackupInv
    }

    override fun onEnable() {
        instance = this
        Metrics(this, 14814)
        Config.load(null)
        BackupData.load(null)
        CommandManager.register("ebackupinv")
        Listener.register()
        val d = Config.check * 60 * 60 * 20
        runTaskTimer(d, d, BackupTaskManager::cleanTimeout)
        BackupTaskManager.hotswap()
        Update.init()
        for (line in logo) info(line)
        info("&a加载完成".color())
    }
}