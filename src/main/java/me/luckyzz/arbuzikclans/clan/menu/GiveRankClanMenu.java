package me.luckyzz.arbuzikclans.clan.menu;

import me.luckkyyz.luckapi.chat.input.ChatInputMessageService;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.config.MenuText;
import me.luckyzz.arbuzikclans.config.Messages;

public class GiveRankClanMenu extends RankClanMenu {

    GiveRankClanMenu(MessageConfig<Messages> messageConfig, MessageConfig<MenuText> menuText, ClanMenuService menuService, ChatInputMessageService inputMessageService, ClanMember target) {
        super(ClanMenuType.RANK_GIVE, messageConfig, menuText, menuService, inputMessageService, (target::changeRank));
    }
}
