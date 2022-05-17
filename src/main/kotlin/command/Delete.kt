package top.e404.ebackupinv.command

import org.bukkit.command.CommandSender
import top.e404.ebackupinv.backup.PlayerBackupManager
import top.e404.ebackupinv.util.runTaskAsync
import top.e404.ebackupinv.util.sendMsgWithPrefix

object Delete : AbstractCommand(
    "delete",
    false,
    "ebackupinv.admin"
) {
    override fun help() = """&a/eb delete <玩家ID> &f删除此玩家的所有背包
        |&a/eb delete <玩家ID> <背包备份文件名> &f删除背包
    """.trimMargin()

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
        when (args.size) {
            2 -> {
                val last = args.last()
                PlayerBackupManager.index.forEach {
                    if (it.name.startsWith(last, true)) complete.add(it.name)
                }
            }
            3 -> {
                val last = args.last()
                PlayerBackupManager.index.forEach {
                    if (it.name.startsWith(last, true)) complete.add(it.name)
                }
            }
        }
    }

    override fun onCommand(
        sender: CommandSender,
        args: Array<out String>,
    ) {
        val index = PlayerBackupManager[args[1]]
        if (index == null) {
            sender.sendMsgWithPrefix("&c不存在玩家&6${args[1]}&c数据")
            return
        }
        if (args.size == 2) {
            val count = index.backups.size
            index.backups.clear()
            PlayerBackupManager.scheduleSave()
            sender.sendMsgWithPrefix("&f已删除玩家&6${args[1]}&f的${count}个备份")
            return
        }
        if (args.size == 3) {
            val stamp = args[2].toLongOrNull()
            if (stamp == null) {
                sender.sendMsgWithPrefix("&c${args[2]}不是有效的备份名")
                return
            }
            if (!index.backups.remove(stamp)) {
                sender.sendMsgWithPrefix("&c玩家&6${args[1]}&c没有名为&6${stamp}&c的备份")
                return
            }
            PlayerBackupManager.scheduleSave()
            val file = index.playerDir.resolve("$stamp.yml")
            if (!file.exists()) {
                sender.sendMsgWithPrefix("&c移除玩家&6${args[1]}&c的无效备份&6${stamp}")
                return
            }
            runTaskAsync { file.delete() }
            sender.sendMsgWithPrefix("&a已删除玩家&6${args[1]}&a的备份&6${args[2]}")
            return
        }
        sender.sendMsgWithPrefix(help())
    }
}