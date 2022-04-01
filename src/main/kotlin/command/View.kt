package top.e404.ebackupinv.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import top.e404.ebackupinv.config.BackupData
import top.e404.ebackupinv.config.BackupData.asTime
import top.e404.ebackupinv.data.Backup.Companion.toInventory
import top.e404.ebackupinv.util.color
import top.e404.ebackupinv.util.sendMsgWithPrefix

object View : AbstractCommand(
    "view",
    true,
    "ebackup.admin"
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
            3 -> complete.addAll(BackupData.data.keys)
            4 -> BackupData.getPlayerBackups(args[2])?.run {
                data.keys.forEach { complete.add(it.toString()) }
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
        val backups = BackupData.getPlayerBackups(args[2])
        if (backups == null) {
            sender.sendMsgWithPrefix("&c未找到玩家&6${args[2]}&c的备份数据")
            return
        }
        val t = args[3].toLongOrNull()
        if (t == null) {
            sender.sendMsgWithPrefix("&c${args[3]}不是有效的备份名")
            return
        }
        val backup = backups.getBackup(t)
        if (backup == null) {
            sender.sendMsgWithPrefix("&c不存在玩家&6${args[1]}&c的名为${args[3]}的备份")
            return
        }
        val inv = backup.run { if (isInv) inv else ec }.toInventory(backup.run {
            "&6${backups.name}&f在&6${time.asTime()}&f的备份".color()
        })
        (sender as Player).openInventory(inv)
        sender.sendMsgWithPrefix("&a已打开玩家&6${args[2]}&a的名为&6${args[3]}&a的${if (isInv) "背包" else "末影箱"}")
    }
}