package me.luckyzz.arbuzikclans.config;

import me.luckkyyz.luckapi.config.MessagePath;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Messages implements MessagePath {

    CLAN_RENAME_SUCCESS_LOCAL("clan.rename.success.local", "&fКлан, в котором Вы состоите был переименован игроком &c%name%&f! &c%old_name% &7&l> &c%new_name%"),
    CLAN_RENAME_SUCCESS_EXECUTOR("clan.rename.success.executor", "&fВы успешно переименовали клан! &c%old_name% &7&l> &c%new_name%"),
    CLAN_RENAME_SUCCESS_GLOBAL("clan.rename.success.global", "&fКлан &c%old_name% &fбыл переименован игроком &c%name%&f. Новое название: &c%new_name%"),
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
    CLAN_CREATE_ALREADY_CLAN("clan.create.alreadyInClan", "&cВы уже состоите в клане!");

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
