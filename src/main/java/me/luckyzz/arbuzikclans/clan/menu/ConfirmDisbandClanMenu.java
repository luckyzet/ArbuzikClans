package me.luckyzz.arbuzikclans.clan.menu;

import me.luckkyyz.luckapi.chat.input.ChatInputMessageService;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.config.MenuText;
import me.luckyzz.arbuzikclans.config.Messages;

public class ConfirmDisbandClanMenu extends ConfirmClanMenu {

    ConfirmDisbandClanMenu(MessageConfig<Messages> messageConfig, MessageConfig<MenuText> menuText, ClanMenuService menuService, ClanMember member) {
        super(ClanMenuType.CONFIRM_DISBAND, messageConfig, menuText, menuService, () -> member.getClan().disband(member), () -> {
        });
    }

}
