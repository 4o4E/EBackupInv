package top.e404.ebackupinv.command

import top.e404.ebackupinv.EBackupInv

object CommandManager : AbstractCommandManager(
    EBackupInv.instance,
    listOf(
        Reload,
        Save,
        ListInv,
        View,
        Delete,
        SaveMe,
    )
)