<div align="center">

# EBackupInv

> 基于BukkitAPI的背包、末影箱备份插件, 适用于Spigot或Paper以及其他绝大多数Bukkit的下游分支核心，支持设置备份间隔，备份自动清理

[![Release](https://img.shields.io/github/v/release/4o4E/EBackupInv?label=Release)](https://github.com/4o4E/EBackupInv/releases/latest)
[![Downloads](https://img.shields.io/github/downloads/4o4E/EBackupInv/total?label=Download)](https://github.com/4o4E/EBackupInv/releases)

</div>

## 指令

> 插件主命令为`ebackupinv`，包括缩写`ebinv`、`ebi`、`eb`，如果与其他插件指令冲突，请使用`ecbackupinv`
1. `eb reload` 重载插件
2. `eb save <玩家ID>` 保存玩家的背包
3. `eb list` 查看所有备份过的玩家名
4. `eb list <玩家id>` 查看所有备份
5. `eb delete <玩家ID>` 删除此玩家的所有背包
6. `eb delete <玩家ID> <背包备份文件名>` 删除背包
7. `eb savefile` 手动调用一次文件备份

## 权限

- `ebackupinv.admin` 允许使用插件指令

## 配置

> 插件默认配置见[配置文件](src/main/resources/config.yml)，配置文件内均有注释描述用法和含义

## 下载

- [最新版](https://github.com/4o4E/EBackupInv/releases/latest)

## 更新记录

```
2022.04.01 - 1.0.0 发布插件
2022.04.01 - 1.0.1 添加更新检查
2022.04.03 - 1.0.2 添加死亡时创建备份的设置
2022.04.12 - 1.0.3 修复bug
2022.04.13 - 1.0.4 修改为异步保存, 减少性能占用, 修复bug
2022.04.14 - 1.0.5 修复读取默认配置文件时未关闭的流
2022.04.17 - 1.0.6 添加切换显示debug日志的选项
2022.04.26 - 1.0.7 添加saveme指令, 添加检查更新开关, 优化&修复bug, 修复view指令补全内容过多导致的补全失效
2022.04.27 - 1.0.8 修复遗漏的名字补全
2022.05.02 - 1.0.9 添加文件备份, 优化代码格式, 修复可能导致ConcurrentModificationException的问题
2022.05.02 - 1.1.0 修复bug
2022.05.17 - 1.2.0 拆分备份文件, 仅保存备份索引以减少内存占用. 提供更便捷的指令反馈(悬浮文本和点击). 注意: 修改备份的储存格式(提供了旧版本文件的自动转换), 兼容版本变为1.15.2+(不包括1.15.1)
2022.07.21 - 1.2.2 修复立即复活时由于异步获取背包物品导致获取的物品列表为空
```

## bstats

![bstats](https://bstats.org/signatures/bukkit/EBackupInv.svg)
