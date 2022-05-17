package top.e404.ebackupinv.command

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.CommandSender
import top.e404.ebackupinv.backup.PlayerBackup.Companion.formatAsTimeStamp
import top.e404.ebackupinv.config.Config
import top.e404.ebackupinv.backup.PlayerBackupManager
import top.e404.ebackupinv.util.color
import top.e404.ebackupinv.util.hover
import top.e404.ebackupinv.util.sendMsgWithPrefix

object ListInv : AbstractCommand(
    "list",
    false,
    "ebackupinv.admin"
) {
    private val spacing = TextComponent("&7, ".color())
    override fun help() = """&a/eb list &f查看所有备份过的玩家名
        |&a/eb list <玩家id> &f查看所有备份
    """.trimMargin()

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>,
        complete: MutableList<String>,
    ) {
        if (args.size == 2) PlayerBackupManager.index.forEach { complete.add(it.name) }
    }

    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        if (args.size == 1) {
            val players = PlayerBackupManager.index
            if (players.isEmpty()) {
                sender.sendMsgWithPrefix("&c还没有备份数据")
                return
            }
            val iterator = PlayerBackupManager.index.sortedBy { it.name }.map { index ->
                val list = index.backups.joinToString("\n") { "&6$it &7[&f${it.formatAsTimeStamp()}&7]" }
                "&a${index.name}".color().hover("&fuuid:\n${index.uuid}\n备份:\n$list\n点击粘贴查看指令".color()).apply {
                    clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ebackupinv list ${index.name}")
                }
            }.iterator()
            val component = mutableListOf(TextComponent("${Config.prefix} &6备份过的玩家: &7[".color()))
            while (true) {
                component.add(iterator.next())
                if (!iterator.hasNext()) break
                component.add(spacing)
            }
            component.add(TextComponent("&7]".color()))
            sender.spigot().sendMessage(*component.toTypedArray())
            return
        }
        if (args.size == 2) {
            val index = PlayerBackupManager.index.firstOrNull { it.name.equals(args[1], true) }
            if (index == null) {
                sender.sendMsgWithPrefix("&c不存在玩家&6${args[1]}&c数据")
                return
            }
            val longs = index.backups
            if (longs.isEmpty()) {
                sender.sendMsgWithPrefix("&c还没有玩家&6${args[1]}&c的备份数据")
                return
            }
            val iterator = longs.map { time ->
                val hover = "&f玩家${index.name}的备份\n备份于&6${time.formatAsTimeStamp()}\n&a点击粘贴查看指令".color()
                "&2$time".color().hover(hover).apply {
                    clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ebackupinv view inv ${index.name} $time")
                }
            }.iterator()
            val component = mutableListOf(TextComponent("${Config.prefix} &f玩家&6${args[1]}&f的备份有&7[".color()))
            while (true) {
                component.add(iterator.next())
                if (!iterator.hasNext()) break
                component.add(spacing)
            }
            component.add(TextComponent("&7]".color()))
            sender.spigot().sendMessage(*component.toTypedArray())
            return
        }
        sender.sendMsgWithPrefix(help())
    }
}