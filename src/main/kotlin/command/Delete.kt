package top.e404.ebackupinv.command

import org.bukkit.command.CommandSender
import top.e404.ebackupinv.config.BackupData
import top.e404.ebackupinv.util.sendMsgWithPrefix

object Delete : AbstractCommand(
    "delete",
    false,
    "ebackupinv.admin"
) {
    override fun help() = """&a/eb delete <玩家ID> &f删除此玩家的所有背包
        |&a/eb delete <玩家ID> <背包备份文件名> &f删除背包""".trimMargin()

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
        when (args.size) {
            2 -> complete.addAll(BackupData.data.keys)
            3 -> BackupData.getPlayerBackupsByName(args[1])?.run {
                for (key in data.keys) complete.add(key.toString())
            }
        }
    }

    override fun onCommand(
        sender: CommandSender,
        args: Array<out String>,
    ) {
        val backups = BackupData.getPlayerBackups(args[1])
        if (backups == null) {
            sender.sendMsgWithPrefix("&c不存在玩家&6${args[1]}&c数据")
            return
        }
        if (args.size == 2) {
            val count = backups.data.size
            backups.data.clear()
            BackupData.scheduleSave()
            sender.sendMsgWithPrefix("&f已删除玩家&6${args[1]}&f的${count}个备份")
            return
        }
        if (args.size == 3) {
            val t = args[2].toLongOrNull()
            if (t == null) {
                sender.sendMsgWithPrefix("&c${t}不是有效的备份名")
                return
            }
            val backup = backups.data.remove(t)
            if (backup == null) {
                sender.sendMsgWithPrefix("&c不存在玩家&6${args[1]}&c的名为${t}的备份")
                return
            }
            BackupData.scheduleSave()
            sender.sendMsgWithPrefix("&a已删除${backup.info()}")
            return
        }
        sender.sendMsgWithPrefix(help())
    }
}