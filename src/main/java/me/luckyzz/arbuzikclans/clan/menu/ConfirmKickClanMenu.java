package me.luckyzz.arbuzikclans.clan.menu;

import me.luckkyyz.luckapi.chat.input.ChatInputMessageService;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.config.MenuText;
import me.luckyzz.arbuzikclans.config.Messages;

public class ConfirmKickClanMenu extends ConfirmClanMenu {

    ConfirmKickClanMenu(MessageConfig<Messages> messageConfig, MessageConfig<MenuText> menuText, ClanMenuService menuService, ChatInputMessageService inputMessageService, ClanMember member, ClanMember whoKick) {
        super(ClanMenuType.CONFIRM_KICK, messageConfig, menuText, menuService, inputMessageService, () -> member.getClan().getMembers().removeMember(member, whoKick), () -> {
        });
    }

}
