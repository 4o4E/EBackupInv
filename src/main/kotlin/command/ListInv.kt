package top.e404.ebackupinv.command

import org.bukkit.command.CommandSender
import top.e404.ebackupinv.config.BackupData
import top.e404.ebackupinv.util.sendMsgWithPrefix

object ListInv : AbstractCommand(
    "list",
    false,
    "ebackup.admin"
) {
    override fun help() = """&a/eb list &f查看所有备份过的玩家名
        |&a/eb list <玩家id> &f查看所有备份""".trimMargin()

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
        if (args.size == 2) complete.addAll(BackupData.data.keys)
    }

    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        if (args.size == 1) {
            val players = BackupData.data.keys
            if (players.isEmpty()) {
                sender.sendMsgWithPrefix("&c还没有备份数据")
                return
            }
            sender.sendMsgWithPrefix("""&a备份过的玩家: &7[
                |  &6${BackupData.data.keys.joinToString("&7,\n  &6")}
                |&7]""".trimMargin())
            return
        }
        if (args.size == 2) {
            val backups = BackupData.data[args[1]]
            if (backups == null) {
                sender.sendMsgWithPrefix("&c不存在玩家&6${args[1]}&c数据")
                return
            }
            val bps = backups.data.values
            if (bps.isEmpty()) {
                sender.sendMsgWithPrefix("&c还没有玩家&6${args[1]}&c的备份数据")
                return
            }
            sender.sendMsgWithPrefix("""&f玩家&6${args[1]}&f的备份有&7[&2
                |  ${bps.joinToString("&7,\n  &2") { it.info() }}
                |&7]""".trimMargin())
            return
        }
        sender.sendMsgWithPrefix(help())
    }
}