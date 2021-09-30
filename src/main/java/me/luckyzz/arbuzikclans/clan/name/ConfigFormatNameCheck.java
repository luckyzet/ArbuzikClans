package me.luckyzz.arbuzikclans.clan.name;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.util.color.ColorUtils;
import me.luckyzz.arbuzikclans.config.ClanConfig;
import me.luckyzz.arbuzikclans.config.Messages;
import org.bukkit.entity.Player;

public class ConfigFormatNameCheck implements FormatNameCheck {

    private final ClanConfig config;
    private final MessageConfig<Messages> messageConfig;

    public ConfigFormatNameCheck(ClanConfig config, MessageConfig<Messages> messageConfig) {
        this.config = config;
        this.messageConfig = messageConfig;
    }

    @Override
    public boolean checkName(Player player, String name) {
        if (!config.getBoolean("name.colors") && name.contains(ColorUtils.ALTERNATIVE_CODE_STRING)) {
            if (player != null) {
                messageConfig.getMessage(Messages.CLAN_CREATE_NOT_COLORS).send(player);
            }
            return false;
        }

        if(name.length() < config.getInt("name.min") || name.length() > config.getInt("name.max")) {
            if (player != null) {
                messageConfig.getMessage(Messages.NAME_LENGTH).send(player);
            }
            return false;
        }
        if(!config.getBoolean("name.spaces") && name.contains(" ")) {
            if (player != null) {
                messageConfig.getMessage(Messages.NAME_SPACE).send(player);
            }
            return false;
        }
        return true;
    }
}
