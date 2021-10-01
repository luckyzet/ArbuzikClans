package me.luckyzz.arbuzikclans.config;

import me.luckkyyz.luckapi.config.MessagePath;

public enum MenuText implements MessagePath {

    MAIN_MENU_TITLE("mainMenu.title", "&fКлан &7[&c%name%&7]"),
    MAIN_MENU_COMPASS_NAME("mainMenu.compass.name", "&fКлан [&c%name%&f]"),
    MAIN_MENU_COMPASS_LORE("mainMenu.compass.lore", "&fВы состоите в этом клане!\n&fВаш ранг: &c%rank%\n\n&fНазвание: &c%name%\n&fСоздан: &c%date%\n&fID: &c%id%\n&fУчастников: &c%members_all%&7/&c%members_max%\n&fОнлайн участников: &c%members_online%&7/&c%members_max%"),
    MAIN_MENU_BASE_NAME("mainMenu.base.name", "&7Клановая база"),
    MAIN_MENU_BASE_LORE("mainMenu.base.lore", "\n&fИнформация о базе Вашего клана"),
    BASE_MENU_TITLE("baseMenu.title", "&fКлановая база"),
    BACK_BUTTON_NAME("backButton.name", "&7Вернуться обратно"),
    BACK_BUTTON_LORE("backButton.lore", "\n&fВозвращает Вас в предыдущее меню"),
    BASE_MENU_COMPASS_NAME("baseMenu.compass.name", "&7Информация о клановой базе"),
    BASE_MENU_COMPASS_LORE("baseMenu.compass.lore", "\n&fКлановая база: &c%state%\n&fМир: &c%world%\n&fЦентр: &c%center%"),
    BASE_MENU_GOLD_NAME("baseMenu.gold.name", "&7Доступ к клановому региону"),
    BASE_MENU_GOLD_LORE("baseMenu.gold.lore", "\n&fОткрывать сундуки: &c%access_chest%\n&fЛомать блоки: &c%access_blocks%\n&fСтавить блоки: &c%access_blocks%"),
    BASE_MENU_ENDER_PEARL_NAME("baseMenu.ender.name", "&7Телепорт на базу"),
    BASE_MENU_ENDER_PEARL_LORE("baseMenu.ender.lore", "\n&fМожете телепортироваться: &c%can_teleport%"),
    BASE_MENU_STONE_NAME("baseMenu.stone.name", "&7Доступ к региону (Блоки)"),
    BASE_MENU_STONE_LORE("baseMenu.stone.lore", "\n&fВключить/выключить доступ для всех соклановцев к региону\n&fТекущее состояние: &c%state_blocks%"),
    BASE_MENU_CHEST_NAME("baseMenu.chest.name", "&7Доступ к региону (Сундуки)"),
    BASE_MENU_CHEST_LORE("baseMenu.chest.lore", "\n&fВключить/выключить доступ для всех соклановцев к сундукам региона\n&fТекущее состояние: &c%state_chest%"),
    BASE_MENU_FEATHER_NAME("baseMenu.feather.name", "&7Игроки с доступом к региону (Блоки)"),
    BASE_MENU_FEATHER_LORE("baseMenu.feather.lore", "\n&fИгроки: &c%blocks_whitelist%\n&fЧтобы добавить/удалить игрока &7- &c/clan region blocks [Никнейм]"),
    BASE_MENU_FLINT_NAME("baseMenu.flint.name", "&7Игроки с доступом к региону (Сундуки)"),
    BASE_MENU_FLINT_LORE("baseMenu.flint.lore", "\n&fИгроки: &c%chests_whitelist%\n&fЧтобы добавить/удалить игрока &7- &c/clan region chests [Никнейм]"),
    RANK_MENU_COAL_NAME("rankMenu.coal.name", "%prefix%"),
    RANK_MENU_COAL_GIVE_LORE("rankMenu.coal.giveLore", "&fНажми, чтобы установить игроку этот ранг"),
    RANK_MENU_COAL_CHANGE_LORE("rankMenu.coal.changeLore", "&fНажми, чтобы изменить префикс этого ранга"),
    RANK_MENU_IRON_NAME("rankMenu.iron.name", "%prefix%"),
    RANK_MENU_IRON_GIVE_LORE("rankMenu.iron.giveLore", "&fНажми, чтобы установить игроку этот ранг"),
    RANK_MENU_IRON_CHANGE_LORE("rankMenu.iron.changeLore", "&fНажми, чтобы изменить префикс этого ранга"),
    RANK_MENU_REDSTONE_NAME("rankMenu.redstone.name", "%prefix%"),
    RANK_MENU_REDSTONE_GIVE_LORE("rankMenu.redstone.giveLore", "&fНажми, чтобы установить игроку этот ранг"),
    RANK_MENU_REDSTONE_CHANGE_LORE("rankMenu.redstone.changeLore", "&fНажми, чтобы изменить префикс этого ранга"),
    RANK_MENU_GOLD_NAME("rankMenu.gold.name", "%prefix%"),
    RANK_MENU_GOLD_GIVE_LORE("rankMenu.gold.giveLore", "&fНажми, чтобы установить игроку этот ранг"),
    RANK_MENU_GOLD_CHANGE_LORE("rankMenu.gold.changeLore", "&fНажми, чтобы изменить префикс этого ранга"),
    RANK_MENU_LAPIS_NAME("rankMenu.lapis.name", "%prefix%"),
    RANK_MENU_LAPIS_GIVE_LORE("rankMenu.lapis.giveLore", "&fНажми, чтобы установить игроку этот ранг"),
    RANK_MENU_LAPIS_CHANGE_LORE("rankMenu.lapis.changeLore", "&fНажми, чтобы изменить префикс этого ранга"),
    RANK_MENU_DIAMOND_NAME("rankMenu.diamond.name", "%prefix%"),
    RANK_MENU_DIAMOND_GIVE_LORE("rankMenu.diamond.giveLore", "&fНажми, чтобы установить игроку этот ранг"),
    RANK_MENU_DIAMOND_CHANGE_LORE("rankMenu.diamond.changeLore", "&fНажми, чтобы изменить префикс этого ранга"),
    RANK_MENU_EMERALD_NAME("rankMenu.emerald.name", "%prefix%"),
    RANK_MENU_EMERALD_GIVE_LORE("rankMenu.emerald.giveLore", "&fНажми, чтобы установить игроку этот ранг"),
    RANK_MENU_EMERALD_CHANGE_LORE("rankMenu.emerald.changeLore", "&fНажми, чтобы изменить префикс этого ранга"),
    RANK_MENU_GIVE_TITLE("rankMenu.give.title", "&fВыдать ранг игроку"),
    RANK_MENU_CHANGE_TITLE("rankMenu.give.title", "&fИзменить префикс ранга"),
    MEMBER_MENU_TITLE("memberMenu.title", "&fУчастники клана"),
    MEMBER_MENU_HEAD_NAME("memberMenu.head.name", "%name%"),
    MEMBER_MENU_HEAD_LORE("memberMenu.head.lore", "\n&fРанг: &c%rank%\n&fПоследний заход: &c%online%\n&fПКМ, чтобы выгнать игрока\n&fЛКМ, чтобы изменить ранг"),
    CONFIRM_MENU_YES_NAME("confirmMenu.yes.name", "&aДА"),
    CONFIRM_MENU_NO_NAME("confirmMenu.no.name", "&aНЕТ"),
    CONFIRM_MENU_TITLE("confirmMenu.title", "&fПодтверждение действия"),
    MAIN_MENU_HEAD_NAME("mainMenu.head.name", "&7Участники"),
    MAIN_MENU_HEAD_LORE("mainMenu.head.lore", "\n&fСписок участников, управление их рангами и прочее"),
    QUEST_MENU_TITLE("questMenu.title", "&fЕжедневные квесты"),
    QUEST_MENU_ITEM_NAME("questMenu.item.name", "%name%"),
    QUEST_MENU_ITEM_LORE("questMenu.item.lore", "\n&fСтатус: &c%state%\n&fОсталось: &c%need% шт.\n&fНаграда: &c%reward_coins%"),
    MAIN_MENU_QUEST_NAME("mainMenu.quest.name", "&7Квесты"),
    MAIN_MENU_QUEST_LORE("mainMenu.quest.lore", "\n&fСписок ваших ежедневных квестов"),
    BASE_MENU_STICK_NAME("baseMenu.stick.name", "&7Выдать блок региона"),
    BASE_MENU_STICK_LORE("baseMenu.stick.lore", "\n&fНажмите, для получения блока региона");

    private final String path, defaultValue;

    MenuText(String path, String defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
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
