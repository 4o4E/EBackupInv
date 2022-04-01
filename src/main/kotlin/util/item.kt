package top.e404.ebackupinv.util

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * 获得Lore
 *
 * @return Lore, 若为空则返回空列表
 */
fun ItemStack.getLore() =
    itemMeta?.lore ?: emptyList()

/**
 * 设置lore
 *
 * @param lore Lore
 */
fun ItemStack.setLore(lore: List<String>) =
    editItemMeta { this.lore = lore }

fun ItemStack.editItemMeta(block: ItemMeta.() -> Unit) {
    val im = itemMeta ?: ItemStack(type).itemMeta!!
    im.block()
    itemMeta = im
}