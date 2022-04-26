package top.e404.ebackupinv.update

import com.google.gson.JsonParser
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import top.e404.ebackupinv.EBackupInv
import top.e404.ebackupinv.config.Config
import top.e404.ebackupinv.listener.EListener
import top.e404.ebackupinv.util.info
import top.e404.ebackupinv.util.runTaskTimerAsync
import top.e404.ebackupinv.util.sendMsgWithPrefix
import top.e404.ebackupinv.util.warn
import java.net.URL

object Update : EListener {
    private const val url = "https://api.github.com/repos/4o4E/EBackupInv/releases"
    private const val mcbbs = "https://www.mcbbs.net/thread-1321213-1-1.html"
    private const val github = "https://github.com/4o4E/EBackupInv"
    private val jp = JsonParser()
    private var latest: String? = null
    private lateinit var instance: EBackupInv
    private lateinit var nowVer: String

    // 返回最新的版本
    private fun getLatest() = jp.parse(URL(url).readText())
        .asJsonArray[0]
        .asJsonObject["tag_name"]
        .asString!!

    private fun String.asVersion() = replace(".", "").toInt()

    fun init() {
        instance = EBackupInv.instance
        nowVer = instance.description.version
        register()
        runTaskTimerAsync(0, 20 * 60 * 60 * 6) {
            if (!Config.update) return@runTaskTimerAsync
            runCatching {
                val v = getLatest()
                val now = instance.description.version
                if (v.asVersion() > now.asVersion()) {
                    latest = v
                    info(
                        """&f插件有更新哦, 当前版本: &c$nowVer&f, 最新版本: &a$latest
                            |&f更新发布于:&b $mcbbs
                            |&f开源于:&b $github
                        """.trimMargin()
                    )
                    return@runCatching
                }
            }.onFailure {
                warn("检查版本更新时出现异常, 若需要手动更新请前往&b $mcbbs")
            }
            info("当前版本: &a${nowVer}已是最新版本")
        }
    }

    @EventHandler
    fun onOpJoinGame(event: PlayerJoinEvent) = event.run {
        if (!player.isOp || latest == null || !Config.update) return@run
        player.sendMsgWithPrefix(
            """&f插件有更新哦, 当前版本: &c$nowVer&f, 最新版本: &a$latest
                |&f更新发布于:&b $mcbbs
                |&f开源于:&b $github
            """.trimMargin()
        )
    }
}