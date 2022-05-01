package top.e404.ebackupinv.command

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import top.e404.ebackupinv.EBackupInv
import top.e404.ebackupinv.backup.FileBackupTaskManager
import top.e404.ebackupinv.util.sendMsgWithPrefix
import top.e404.ebackupinv.util.warn

object SaveFile : AbstractCommand(
    "savefile",
    false,
    "ebackupinv.admin"
) {
    override fun help() = """&a/eb savefile &f立即触发一次文件保存""".trimMargin()

    override fun onCommand(
        sender: CommandSender,
        args: Array<out String>,
    ) {
        sender.sendMsgWithPrefix("&a开始保存")
        Bukkit.getScheduler().runTaskAsynchronously(EBackupInv.instance, Runnable {
            try {
                FileBackupTaskManager.backup()
                sender.sendMsgWithPrefix("&a已保存")
            } catch (t: Throwable) {
                sender.sendMsgWithPrefix("&c保存时出现异常")
                warn("保存时出现异常", t)
            }
        })
    }
}