package me.luckyzz.arbuzikclans.clan.menu;

import me.luckkyyz.luckapi.chat.input.ChatInputMessageService;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckyzz.arbuzikclans.config.MenuText;
import me.luckyzz.arbuzikclans.config.Messages;

public abstract class AbstractClanMenu implements ClanMenu {

    protected final ClanMenuType menuType;
    protected final MessageConfig<Messages> messageConfig;
    protected final MessageConfig<MenuText> menuText;
    protected final ClanMenuService menuService;

    protected AbstractClanMenu(ClanMenuType menuType, MessageConfig<Messages> messageConfig, MessageConfig<MenuText> menuText, ClanMenuService menuService) {
        this.menuType = menuType;
        this.messageConfig = messageConfig;
        this.menuText = menuText;
        this.menuService = menuService;
    }

    @Override
    public ClanMenuType getType() {
        return menuType;
    }

}
