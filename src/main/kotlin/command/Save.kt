package top.e404.ebackupinv.command

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import top.e404.ebackupinv.config.BackupData
import top.e404.ebackupinv.util.sendMsgWithPrefix

object Save : AbstractCommand(
    "save",
    false,
    "ebackup.admin"
) {
    override fun help() = """&a/eb save <玩家ID> &f保存玩家的背包""".trimMargin()

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
        if (args.size == 2) complete.addOnlinePlayers()
    }

    override fun onCommand(
        sender: CommandSender,
        args: Array<out String>,
    ) {
        if (args.size != 2) {
            sender.sendMsgWithPrefix(help())
            return
        }
        val player = Bukkit.getPlayer(args[1])
        if (player == null) {
            sender.sendMsgWithPrefix("&c玩家&6${args[1]}&c当前不在线")
            return
        }
        val backup = BackupData.savePlayer(player)
        sender.sendMsgWithPrefix("&a已保存, ${backup.info()}")
    }
}