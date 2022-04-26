package top.e404.ebackupinv.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import top.e404.ebackupinv.config.BackupData
import top.e404.ebackupinv.config.Config
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
        val backup = BackupData.savePlayer(sender)
        if (backup == null) {
            sender.sendMsgWithPrefix("&c空背包将不会被保存")
            return
        }
        cd[sender.uniqueId] = now
        sender.sendMsgWithPrefix("&a已保存, 背包共${backup.inv.size}件物品, 末影箱共${backup.ec.size}件物品")
    }
}