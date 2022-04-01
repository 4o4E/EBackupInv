package top.e404.ebackupinv.command

import org.bukkit.command.CommandSender
import top.e404.ebackupinv.config.Config
import top.e404.ebackupinv.util.color
import top.e404.ebackupinv.util.sendMsgWithPrefix
import top.e404.ebackupinv.util.sendOrElse
import top.e404.ebackupinv.util.warn

object Reload : AbstractCommand(
    "reload",
    false,
    "ebackup.admin"
) {
    override fun help() = "&a/eb reload &f重载插件"

    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        try {
            Config.load(sender)
            sender.sendMsgWithPrefix("&a重载完成")
        } catch (t: Throwable) {
            val s = "&c配置文件`config.yml`格式错误".color()
            sender.sendOrElse(s) {
                warn(s, t)
            }
        }
    }
}