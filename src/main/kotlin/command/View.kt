package top.e404.ebackupinv.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import top.e404.ebackupinv.backup.PlayerBackupManager
import top.e404.ebackupinv.backup.PlayerBackup.Companion.formatAsTimeStamp
import top.e404.ebackupinv.backup.PlayerBackup.Companion.toInventory
import top.e404.ebackupinv.util.color
import top.e404.ebackupinv.util.sendMsgWithPrefix

object View : AbstractCommand(
    "view",
    true,
    "ebackupinv.admin"
) {
    override fun help() = "&a/eb view <inv/ec> <玩家id> <备份文件名> &f浏览玩家备份的背包/末影箱(可以取出, 但修改结果不写入备份)"

    private val type = listOf("inv", "ec")

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
        when (args.size) {
            2 -> complete.addAll(type)
            3 -> PlayerBackupManager.index.forEach { index ->
                if (index.name.startsWith(args[2], true)) complete.add(index.name)
            }
            4 -> {
                val index = PlayerBackupManager[args[2]] ?: return
                for (stamp in index.backups) {
                    val s = stamp.toString()
                    if (s.startsWith(args[3], true)) complete.add(s)
                }
            }
        }
    }

    override fun onCommand(
        sender: CommandSender,
        args: Array<out String>,
    ) {
        if (args.size != 4) {
            sender.sendMsgWithPrefix(help())
            return
        }
        val isInv = when (args[1].lowercase()) {
            "inv" -> true
            "ec" -> false
            else -> {
                sender.sendMsgWithPrefix("&c背包类型仅支持&ainv&7(背包)&c和&aec&7(末影箱)")
                return
            }
        }
        val index = PlayerBackupManager.index.firstOrNull { it.name.equals(args[2], true) }
        if (index == null) {
            sender.sendMsgWithPrefix("&c未找到玩家&6${args[2]}&c的备份数据")
            return
        }
        val stamp = args[3].toLongOrNull()
        if (stamp == null) {
            sender.sendMsgWithPrefix("&c${args[3]}不是有效的备份名")
            return
        }
        if (stamp !in index.backups) {
            sender.sendMsgWithPrefix("&c不存在备份${args[3]}")
            return
        }

        val backup = index.getByStamp(stamp)
        if (backup == null) {
            sender.sendMsgWithPrefix("&c不存在玩家&6${args[1]}&c的名为${args[3]}的备份")
            return
        }
        val inv = backup.run { if (isInv) inv else ec }.toInventory(backup.run {
            "&6${index.name}&f在&6${time.formatAsTimeStamp()}&f的备份".color()
        })
        (sender as Player).openInventory(inv)
        sender.sendMsgWithPrefix("&a已打开玩家&6${args[2]}&a的名为&6${args[3]}&a的${if (isInv) "背包" else "末影箱"}")
    }
}