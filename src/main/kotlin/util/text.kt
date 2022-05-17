package top.e404.ebackupinv.util

import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent

fun String.hover(hover: String) = TextComponent(color()).apply {
    hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(TextComponent(hover)))
}