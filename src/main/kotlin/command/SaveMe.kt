package top.e404.ebackupinv.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import top.e404.ebackupinv.config.Config
import top.e404.ebackupinv.backup.PlayerBackupManager.triggerBackup
import top.e404.ebackupinv.util.sendMsgWithPrefix
import java.util.*

object SaveMe : AbstractCommand(
    "saveme",
    true,
    "ebackupinv.saveme"
) {
    private val cd = mutableMapOf<UUID, Long>()
    override fun help() = """&a/eb saveme &f创建一次自己背包和末影箱的备份""".trimMargin()
    override fun onCommand(
        sender: CommandSender,
        args: Array<out String>,
    ) {
        sender as Player
        val now = System.currentTimeMillis()
        // 检查cd
        cd[sender.uniqueId]?.let {
            val least = Config.cooldown - (now - it) / 1000
            if (least > 0) {
                sender.sendMsgWithPrefix("&c冷却中, 剩余${least}秒")
                return
            }
        }
        // 更新cd
        sender.triggerBackup()
        sender.sendMsgWithPrefix("&a已保存")
    }
}