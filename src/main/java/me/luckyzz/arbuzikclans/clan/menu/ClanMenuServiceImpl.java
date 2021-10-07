package me.luckyzz.arbuzikclans.clan.menu;

import me.luckkyyz.luckapi.chat.input.ChatInputMessageService;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.upgrade.ClanUpgradeService;
import me.luckyzz.arbuzikclans.config.MenuText;
import me.luckyzz.arbuzikclans.config.Messages;

import java.util.HashMap;
import java.util.Map;

public class ClanMenuServiceImpl implements ClanMenuService {

    private final MessageConfig<Messages> messageConfig;
    private final MessageConfig<MenuText> menuText;
    private final ChatInputMessageService inputMessageService;
    private final ClanUpgradeService upgradeService;

    private final Map<ClanMenuType, ClanMenu> menuMap = new HashMap<>();

    public ClanMenuServiceImpl(MessageConfig<Messages> messageConfig, MessageConfig<MenuText> menuText, ChatInputMessageService inputMessageService, ClanUpgradeService upgradeService) {
        this.messageConfig = messageConfig;
        this.menuText = menuText;
        this.inputMessageService = inputMessageService;
        this.upgradeService = upgradeService;
        reload();
    }

    @Override
    public void reload() {
        menuMap.clear();

        menuMap.put(ClanMenuType.MAIN, new MainClanMenu(messageConfig, menuText, this, inputMessageService));
        menuMap.put(ClanMenuType.BASE, new BaseClanMenu(messageConfig, menuText, this, inputMessageService));
        menuMap.put(ClanMenuType.MEMBERS, new MemberClanMenu(messageConfig, menuText, this, inputMessageService));
        menuMap.put(ClanMenuType.QUESTS, new QuestClanMenu(messageConfig, menuText, this, inputMessageService));
        menuMap.put(ClanMenuType.UPGRADE, new UpgradeClanMenu(messageConfig, menuText, this, inputMessageService, upgradeService));
    }

    @Override
    public ClanMenu getMenu(ClanMenuType type, Object... args) {
        if (type == ClanMenuType.CONFIRM_KICK) {
            return new ConfirmKickClanMenu(messageConfig, menuText, this, inputMessageService, (ClanMember) args[0], (ClanMember) args[1]);
        }
        if (type == ClanMenuType.CONFIRM_DISBAND) {
            return new ConfirmDisbandClanMenu(messageConfig, menuText, this, inputMessageService, (ClanMember) args[0]);
        }
        if (type == ClanMenuType.RANK_GIVE) {
            return new GiveRankClanMenu(messageConfig, menuText, this, inputMessageService, (ClanMember) args[0]);
        }

        return menuMap.get(type);
    }
}
