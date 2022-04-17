package top.e404.ebackupinv.backup

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import top.e404.ebackupinv.config.BackupData
import top.e404.ebackupinv.config.Config
import top.e404.ebackupinv.util.debug
import top.e404.ebackupinv.util.info
import top.e404.ebackupinv.util.runTaskTimer
import top.e404.ebackupinv.util.runTaskTimerAsync

object BackupTaskManager {
    private val tasks = HashMap<Player, BukkitTask>()
    private val uuidRegex = Regex("[a-z0-9]{8}(-[a-z0-9]{4}){3}-[a-z0-9]{12}")

    fun String.isUUID() = uuidRegex.matches(this)

    fun onPlayerQuit(player: Player) {
        tasks.remove(player)?.cancel()
        BackupData.savePlayer(player)
    }

    fun onPlayerJoin(player: Player) {
        tasks[player] = schedule(player)
    }

    /**
     * 计划玩家的定时备份
     *
     * @param player 玩家
     * @return BukkitTask
     */
    fun schedule(player: Player) =
        runTaskTimerAsync(Config.duration, Config.duration) {
            BackupData.savePlayer(player)
        }

    /**
     * 清理过期备份
     */
    fun cleanTimeout() {
        val t = System.currentTimeMillis() - Config.keep * 60 * 60 * 1000
        val count = BackupData.data.values.sumOf { it.cleanTimeout(t) }
        debug("自动清理过期备份共${count}个")
    }

    /**
     * 热重载后将已在线玩家加入备份任务
     */
    fun hotswap() {
        for (player in Bukkit.getOnlinePlayers()) {
            tasks[player] = schedule(player)
        }
    }
}