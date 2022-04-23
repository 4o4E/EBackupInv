<div align="center">

# EBackupInv

> 基于BukkitAPI的背包、末影箱备份插件, 适用于Spigot或Paper以及其他绝大多数Bukkit的下游分支核心，支持设置备份间隔，备份自动清理

[![Release](https://img.shields.io/github/v/release/4o4E/EBackupInv?label=Release)](https://github.com/4o4E/EBackupInv/releases/latest)
[![Downloads](https://img.shields.io/github/downloads/4o4E/EBackupInv/total?label=Download)](https://github.com/4o4E/EBackupInv/releases)

</div>

## 指令

> 插件主命令为`/ebackupinv`，包括缩写`/ebinv`、`/ebi`、`/eb`，如果与其他插件指令冲突，请使用`/ecbackupinv`
1. `/eb reload` 重载插件
2. `/eb save <玩家ID>` 保存玩家的背包
3. `/eb list` 查看所有备份过的玩家名
4. `/eb list <玩家id>` 查看所有备份
5. `/eb delete <玩家ID>` 删除此玩家的所有背包
6. `/eb delete <玩家ID> <背包备份文件名>` 删除背包

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
```

## bstats

![bstats](https://bstats.org/signatures/bukkit/EBackupInv.svg)
