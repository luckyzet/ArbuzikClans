package me.luckyzz.arbuzikclans.config;

import me.luckkyyz.luckapi.config.MessagePath;

import java.util.Arrays;
import java.util.List;

public enum Messages implements MessagePath {

    CLAN_RENAME_SUCCESS_LOCAL("clan.rename.success.local", "&7[&cКлан&7] &fКлан был переименован игроком &c%rank% &c%name%&f! &c%old_name% &7&l> &c%new_name%"),
    CLAN_RENAME_SUCCESS_EXECUTOR("clan.rename.success.executor", "&fВы успешно переименовали клан! &c%old_name% &7&l> &c%new_name%"),
    CLAN_RENAME_SUCCESS_GLOBAL("clan.rename.success.global", "&fКлан &c%old_name% &fбыл переименован игроком &c%rank% &c%name%&f. Новое название: &c%new_name%"),
    CLAN_CREATE_NOT_COLORS("clan.create.notColors", "&cВ названии отключены цветовые коды! Вы не можете их использовать"),
    NOT_ENOUGH_MONEY("notEnoughMoney", "&cУ Вас недостаточно средств! Имеется %balance% из %need_balance% (%need% осталось)"),
    ONLY_PLAYER("onlyPlayer", "&cЭта команда только для игроков!"),
    NO_PERMISSION("noPermission", "&cУ Вас нет прав!"),
    NOT_COMMAND("notCommand", "&cТакой команды не существует!"),
    SOMETHING_WENT_WRONG("somethingWentWrong", "&cЧто-то пошло не так! Обратитесь к Администратору"),
    CLAN_CREATE_USAGE("clan.create.usage", "&cИспользуй - /clan create [Название]"),
    CLAN_CREATE_SUCCESS("clan.create.success", "&fВы успешно создали клан &c%name%&f!"),
    CLAN_HELP("clan.help", Arrays.asList(
            "&7[&cArbuzik&fClans&7] &7&l| &fПомощь по командам:",
            "&c/clan create [Название] &7&l- &fСоздать клан"
    )),
    ALREADY_CLAN("alreadyInClan", "&cВы уже состоите в клане!"),
    ALREADY_CLAN_OTHER("alreadyInClanOther", "&cИгрок %name% уже состоит в клане!"),
    NOT_CLAN("notClan", "&cВы не состоите в клане!"),
    NOT_IN_CLAN("notInClan", "&cИгрок %name% не состоит в клане!"),
    YOURSELF("yourself", "&cВы не можете выполнить это действие по отношению к себе!"),
    NOT_ACCESS("notAccess", "&cУ Вас недостаточно полномочий, чтобы сделать это!"),
    CLAN_CHAT_EMPTY("clan.chat.emptyMessage", "&cВы не можете отправить в чат пустое сообщение!"),
    CLAN_CHAT_FORMAT("clan.chat.format", "&7[&cК.Чат&7] &f%rank% %name%&7: &f%message%"),
    CLAN_CHAT_DISABLED("clan.chat.disabled", "&cКлановый чат выключен!"),
    CLAN_CHAT_ENABLED_EXECUTOR("clan.chat.enabled.executor", "&fВы успешно включили клановый чат!"),
    CLAN_CHAT_ENABLED_LOCAL("clan.chat.enabled.local", "&7[&cКлан&7] &f%rank% %name% &fвключил клановый чат!"),
    CLAN_CHAT_DISABLED_EXECUTOR("clan.chat.disabled.executor", "&fВы успешно выключили клановый чат!"),
    CLAN_CHAT_DISABLED_LOCAL("clan.chat.disabled.local", "&7[&cКлан&7] &f%rank% %name% &fвыключил клановый чат!"),

    CLAN_CHAT_MUTED("clan.chat.muted", "&cВы заглушены в клановом чате! Оставшееся время - %time%. Причина - %reason%"),
    CLAN_CHAT_MUTED_EXECUTOR("clan.chat.muteEnabled.executor", "&fВы успешно выдали мут игроку %name%!"),
    CLAN_CHAT_MUTED_LOCAL("clan.chat.muteEnabled.local", "&7[&cКлан&7] &f%rank% %name% &fвыдал мут в клановом чате на &c%duration% &fигроку &c%rank_muted% &c%name_muted%&f. Причина &7- &c%reason%"),
    CLAN_CHAT_MUTE_NOT_YOURSELF("clan.chat.mute.notYourself", "&cВы не можете замутить сами себя!"),

    CLAN_CHAT_MUTED_ALREADY("clan.chat.mutedAlready", "&cЭтот игрок уже заглушен!"),

    SUCCESS_BUT_ALREADY("successButAlready", "&fУспешно, но ничего не изменилось от этого действия!"),
    CLAN_REGION_ACCESS_BLOCKS_ENABLED_EXECUTOR("clan.region.accessBlocks.enabled.executor", "&fВы успешно включили доступ к региону клана!"),
    CLAN_REGION_ACCESS_BLOCKS_ENABLED_LOCAL("clan.region.accessBlocks.enabled.executor", "&7[&cКлан&7] &c%rank% &c%name% &fвключил доступ к региону клана"),
    CLAN_REGION_ACCESS_BLOCKS_DISABLED_EXECUTOR("clan.region.accessBlocks.disabled.executor", "&fВы успешно выключили доступ к региону клана!"),
    CLAN_REGION_ACCESS_BLOCKS_DISABLED_LOCAL("clan.region.accessBlocks.disabled.executor", "&7[&cКлан&7] &c%rank% &c%name% &fвыключил доступ к региону клана"),
    CLAN_REGION_ACCESS_CHESTS_ENABLED_EXECUTOR("clan.region.accessChests.enabled.executor", "&fВы успешно включили доступ к сундукам региона клана!"),
    CLAN_REGION_ACCESS_CHESTS_ENABLED_LOCAL("clan.region.accessChests.enabled.executor", "&7[&cКлан&7] &c%rank% &c%name% &fвключил доступ к сундукам региона клана"),
    CLAN_REGION_ACCESS_CHESTS_DISABLED_EXECUTOR("clan.region.accessChests.disabled.executor", "&fВы успешно выключили доступ к сундукам региона клана!"),
    CLAN_REGION_ACCESS_CHESTS_DISABLED_LOCAL("clan.region.accessChests.disabled.executor", "&7[&cКлан&7] &c%rank% &c%name% &fвыключил доступ к сундукам региона клана"),
    CLAN_REGION_ACCESS_BLOCKS_WHITELIST_ADD_EXECUTOR("clan.region.accessBlocks.whitelist.add.executor", "&fВы успешно разрешили игроку %name% редактировать блоки в регионе клана!"),
    CLAN_REGION_ACCESS_BLOCKS_WHITELIST_ADD_TARGET("clan.region.accessBlocks.whitelist.add.target", "&7[&cКлан&7] &fИгрок &c%rank% &c%name% &fразрешил вам редактировать блоки в регионе клана!"),
    CLAN_REGION_ACCESS_BLOCKS_WHITELIST_ADD_LOCAL("clan.region.accessBlocks.whitelist.add.local", "&7[&cКлан&7] &fИгрок &c%rank% &c%name% &fразрешил редактировать регион клана игроку &c%target_rank% &c%target_name%"),
    CLAN_REGION_ACCESS_BLOCKS_WHITELIST_REMOVE_EXECUTOR("clan.region.accessBlocks.whitelist.remove.executor", "&fВы успешно запретили игроку %name% редактировать блоки в регионе клана!"),
    CLAN_REGION_ACCESS_BLOCKS_WHITELIST_REMOVE_TARGET("clan.region.accessBlocks.whitelist.remove.target", "&7[&cКлан&7] &fИгрок &c%rank% &c%name% &fзапретил вам редактировать блоки в регионе клана!"),
    CLAN_REGION_ACCESS_BLOCKS_WHITELIST_REMOVE_LOCAL("clan.region.accessBlocks.whitelist.remove.local", "&7[&cКлан&7] &fИгрок &c%rank% &c%name% &fзапретил редактировать регион клана игроку &c%target_rank% &c%target_name%"),
    CLAN_REGION_ACCESS_CHESTS_WHITELIST_ADD_EXECUTOR("clan.region.accessChests.whitelist.add.executor", "&fВы успешно разрешили игроку %rank% %name% открывать сундуки в регионе клана!"),
    CLAN_REGION_ACCESS_CHESTS_WHITELIST_ADD_TARGET("clan.region.accessChests.whitelist.add.target", "&7[&cКлан&7] &fИгрок &c%rank% &c%name% &fразрешил вам открывать сундуки в регионе клана!"),
    CLAN_REGION_ACCESS_CHESTS_WHITELIST_ADD_LOCAL("clan.region.accessChests.whitelist.add.local", "&7[&cКлан&7] &fИгрок &c%rank% &c%name% &fразрешил открывать сундуки игроку &c%target_rank% &c%target_name%"),
    CLAN_REGION_ACCESS_CHESTS_WHITELIST_REMOVE_EXECUTOR("clan.region.accessChests.whitelist.remove.executor", "&fВы успешно запретили игроку %name% открывать сундуки в регионе клана!"),
    CLAN_REGION_ACCESS_CHESTS_WHITELIST_REMOVE_TARGET("clan.region.accessChests.whitelist.remove.target", "&7[&cКлан&7] &fИгрок &c%rank% &c%name% &fзапретил вам открывать сундуки в регионе клана!"),
    CLAN_REGION_ACCESS_CHESTS_WHITELIST_REMOVE_LOCAL("clan.region.accessChests.whitelist.remove.local", "&7[&cКлан&7] &fИгрок &c%rank% &c%name% &fзапретил открывать сундуки игроку &c%target_rank% &c%target_name%"),
    CLAN_REGION_ITEM_GAVE("clan.region.itemGave", "&fВы получили предмет для создания кланового региона!"),
    CLAN_REGION_NOT_ACCESS("clan.region.notAccess", "&cУ Вас нет доступа к региону клана!"),
    CLAN_REGION_CANNOT_CREATE("clan.region.cannotCreate", "&cВы не можете создать регион клана!"),
    CLAN_REGION_CREATE_SUCCESS_EXECUTOR("clan.region.create.success.executor", "&fВы успешно создали регион клана!"),
    CLAN_REGION_CREATE_SUCCESS_LOCAL("clan.region.create.success.local", "&7[&cКлан&7] &c%rank% &c%name% &fсоздал регион клана!"),
    CLAN_REGION_EXISTS("clan.region.exists", "&cКлановый регион уже существует!"),
    CLAN_REGION_NOT_EXISTS("clan.region.notExists", "&cКлановый регион не существует!"),
    CLAN_REGION_BREAK("clan.region.break", "&7[&cКлан&7] &c%rank% &c%name% &fубрал регион клана!"),
    CLAN_REGION_INTERSECTS("clan.region.intersects", "&cВы не можете установить тут регион, т.к. он пересекает чужой регион!"),
    CLAN_INVITE_ALREADY("clan.invite.already", "&cВы уже пригласили этого игрока!"),
    CLAN_INVITE_ALREADY_CLAN("clan.invite.alreadyClan", "&cЭтот игрок уже состоит в клане!"),
    CLAN_INVITE_ACCEPT_ALREADY_CLAN("clan.invite.accept.alreadyClan", "&cВы уже состоите в клане!"),
    CLAN_INVITE_SUCCESS_EXECUTOR("clan.invite.success.executor", "&fВы успешно пригласили игрока &c%name% &fв клан!"),
    CLAN_INVITE_SUCCESS_TARGET("clan.invite.success.target", Arrays.asList(
            "&fВы были приглашены в клан &c%clan% &fигроком &c%name%",
            "&f/clan accept - чтобы принять, /clan deny - чтобы отказаться")
    ),
    CLAN_INVITE_ACCEPT_CANNOT_LEFT("clan.invite.accept.cannotLeft", "&cВы не можете принять запрос в клан, так как тот, кто вас пригласил покинул клан!"),
    CLAN_INVITE_TIME_LEFT("clan.invite.timeLeft", "&cВремя на принятие запроса истекло!"),
    CLAN_INVITE_DENY_SUCCESS_EXECUTOR("clan.invite.deny.success.executor", "&fВы успешно отказались от запроса в клан!"),
    CLAN_INVITE_DENY_SUCCESS_TARGET("clan.invite.deny.success.target", "&fИгрок &c%name% &fотказался от вступления в клан!"),
    CLAN_RANK_PREFIX_CHANGE_EXECUTOR("clan.rank.prefix.change.executor", "&fВы успешно поменяли префикс рангу &c%old_prefix% &f-> &c%prefix%"),
    CLAN_RANK_PREFIX_CHANGE_LOCAL("clan.rank.prefix.change.local", "&7[&cКлан&7] &c%rank% &c%name% &fизменил префикс ранга &c%old_prefix &7- &c%prefix%"),
    CLAN_QUESTS_UPDATED("clan.quests.updated", "&7[&cКлан&7] &fЕжедневные клановые задания обновлены!"),
    CLAN_QUESTS_COUNT_CHANGE("clan.quests.countChange", "&7[&cКлан&7] &fПрогресс одного из ежедневных квестов&c%count%&7/&c%max% &7(&c%percent%&7)"),
    CLAN_QUESTS_FINISHED("clan.quests.finished", "&7[&cКлан&7] &fВы успешно выполнили одно из своих заданий! За это вашему клану была начислена 1 монета!"),
    CLAN_RANK_CANNOT_GIVE("clan.rank.cannotGive", "&7[&cКлан&7] &cВы не можете выдать игроку этот ранг!"),
    CLAN_MEMBER_ADDED("clan.member.added", "&7[&cКлан&7] &c%rank% &c%name% &fдобавил в клан игрока &c%target_rank% &c%target_name%"),
    CLAN_MEMBER_LEFT("clan.member.left", "&7[&cКлан&7] &c%rank% &c%name% &fпокинул клан!"),
    CLAN_MEMBER_KICKED("clan.member.kicked", "&7[&cКлан&7] &c%rank% &c%name% &fизгнал игрока &c%target_rank% &c%target_name%"),
    CLAN_INVITE_FULL("clan.invite.full", "&7[&cКлан&7] &cВы не можете пригласить этого игрока, т.к. клан переполнен!"),
    CLAN_INVITE_ACCEPT_FULL_EXECUTOR("clan.invite.accept.full.executor", "&7[&cКлан&7] &cВы не можете вступить в клан, т.к. клан переполнен!"),
    CLAN_INVITE_ACCEPT_FULL_TARGET("clan.invite.accept.full.target", "&7[&cКлан&7] &cИгрок %name% не может вступить в клан, т.к. клан переполнен!"),
    CLAN_UPGRADE_CANNOT_APPLY("clan.upgrade.cannotApply", "&cВы не можете применить это обновление к клану!"),
    NAME_LENGTH("name.length", "&cДопустимая длина от 4 до 12 символов!"),
    NAME_SPACE("name.space", "&cВы не можете использовать проблеы в названии!"),
    CLAN_REGION_TELEPORT_SUCCESS("clan.region.teleport.success", "&fВы успешно телепортировались на клановую базу!"),
    CLAN_REGION_TELEPORT_FAILED("clan.region.teleport.failed", "&cКлановой базы не существует, поэтому вы не можете телепортироваться!"),
    CLAN_REGION_TELEPORT_FAILED_DANGEROUS("clan.region.teleport.failedDangerous", "&cВы не можете телепортироваться, т.к. на точке телепорта есть блоки, которые могут Вам навредить!"),
    CLAN_REGION_USAGE("clan.region.usage", "&cИспользуй - /clan region blocks/chests [Никнейм]"),
    CLAN_INVITE_USAGE("clan.invite.usage", "&cИспользуй - /clan invite [Никнейм]"),
    NOT_ONLINE("notOnline", "&cЭтот игрок не онлайн!"),
    NOT_INVITE("clan.invite.notInvite", "&cВас никто не приглашал в клан!"),
    QUEST_COINS_GAVE("clan.quests.gaveCoins", "&7[&cКлан&7] &c%rank% &c%name% &fдобавил в козну &c%amount% коинов &fза счет выполнения квеста! (Всего &c%sum_amount% коинов&f)"),
    CLAN_RENAME_USAGE("clan.rename.usage", "&cИспользуй - /clan rename [Название]"),
    CLAN_REGION_SETTING("clan.region.setting", "&fИдет установка региона... Ожидайте!"),
    NOT_YOUR_CLAN("notYourClan", "&cЭтот человек не находится в Вашем клане!"),

    CLAN_MUTE_USAGE("clan.mute.usage", "&cИспользуй - /clan mute [Ник] [Время(2h,1m,3s)] [Причина]"),
    CLAN_MUTE_DURATION_ZERO("clan.mute.durationZero", "&cВремя мута не может быть нулём! (Возможно неправильный формат, пример, 3h,2m)");

    private final String path, defaultValue;

    Messages(String path, String defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }

    Messages(String path, List<String> defaultValues) {
        this.path = path;
        this.defaultValue = String.join("\n", defaultValues);
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String getPath() {
        return path;
    }
}
