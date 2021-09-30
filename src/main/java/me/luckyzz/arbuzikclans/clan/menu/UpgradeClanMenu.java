package me.luckyzz.arbuzikclans.clan.menu;

import me.luckkyyz.luckapi.chat.input.ChatInputMessageService;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.config.MenuText;
import me.luckyzz.arbuzikclans.config.Messages;

public class UpgradeClanMenu extends AbstractClanMenu {

    UpgradeClanMenu(MessageConfig<Messages> messageConfig, MessageConfig<MenuText> menuText, ClanMenuService menuService, ChatInputMessageService inputMessageService) {
        super(ClanMenuType.UPGRADE, messageConfig, menuText, menuService, inputMessageService);
    }

    @Override
    public void openMenu(ClanMember member, Object... args) {
        Clan clan = member.getClan();


    }
}
