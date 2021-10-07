package me.luckyzz.arbuzikclans.clan.menu;

import me.luckkyyz.luckapi.chat.input.ChatInputMessageService;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.menu.MenuSession;
import me.luckkyyz.luckapi.menu.PreparedMenu;
import me.luckkyyz.luckapi.menu.button.ClickCallback;
import me.luckkyyz.luckapi.menu.button.MenuButton;
import me.luckkyyz.luckapi.menu.filling.PatternFillingStrategy;
import me.luckkyyz.luckapi.message.Message;
import me.luckkyyz.luckapi.util.inventory.ChestInventorySize;
import me.luckkyyz.luckapi.util.itemstack.ItemBuilders;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.impl.MemberUpgradeData;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.rank.RankPossibility;
import me.luckyzz.arbuzikclans.clan.upgrade.ClanUpgrade;
import me.luckyzz.arbuzikclans.clan.upgrade.ClanUpgradeService;
import me.luckyzz.arbuzikclans.clan.upgrade.UpgradeType;
import me.luckyzz.arbuzikclans.config.MenuText;
import me.luckyzz.arbuzikclans.config.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.function.BiFunction;

public class UpgradeClanMenu extends AbstractClanMenu {

    private final ClanUpgradeService upgradeService;

    UpgradeClanMenu(MessageConfig<Messages> messageConfig, MessageConfig<MenuText> menuText, ClanMenuService menuService, ChatInputMessageService inputMessageService, ClanUpgradeService upgradeService) {
        super(ClanMenuType.UPGRADE, messageConfig, menuText, menuService, inputMessageService);
        this.upgradeService = upgradeService;
    }

    @Override
    public void openMenu(ClanMember member, Object... args) {
        Clan clan = member.getClan();

        BiFunction<ClanUpgrade, Message, String> placeholders = (upgrade, message) -> message.toAdaptive()
                .placeholder("%name%", upgrade.getItem())
                .toRawText();

        char[] symbols = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', '3', 'r', 's', 't', 'u'};
        PatternFillingStrategy<MenuSession> patternFillingStrategy = new PatternFillingStrategy<>().setPattern(
                "--abcdefg",
                "q-hijklmn",
                "--op3rstu"
        ).withButton('q', new MenuButton(ItemBuilders.newBuilder()
                .setType(Material.CLAY_BALL)
                .setDisplay(menuText.getMessage(MenuText.BACK_BUTTON_NAME).toRawText())
                .setLore(Arrays.asList(menuText.getMessage(MenuText.BACK_BUTTON_LORE).toRawText().split("\n")))
                .create(), new ClickCallback() {
            @Override
            public void processClick(Player player, ClickType ignored1, int ignored2) {
                menuService.getMenu(ClanMenuType.MAIN).openMenu(member);
            }
        }));

        if(member.hasPossibility(RankPossibility.UPGRADE)) {
            int i = 0;
            for(ClanUpgrade upgrade : upgradeService.getUpgrades()) {
                if(clan.getUpgrades().getUpgrades().contains(upgrade)) {
                    continue;
                }

                if(upgrade.getType() == UpgradeType.MEMBER_SLOTS) {
                    int slotCount = clan.getMembers().getMaxMembers();

                    if(slotCount >= ((MemberUpgradeData) upgrade.getData()).getAmount()) {
                        continue;
                    }
                }

                patternFillingStrategy.withButton(symbols[i++], new MenuButton(upgrade.getItem(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType type, int ignored) {
                        clan.getUpgrades().addUpgrade(upgrade, member);
                    }
                }));
            }
        }
        int i = 0;
        for(ClanUpgrade upgrade : clan.getUpgrades().getUpgrades()) {
            patternFillingStrategy.withButton(symbols[i++], new MenuButton(upgrade.getItem(), new ClickCallback() {
                @Override
                public void processClick(Player player, ClickType type, int ignored) {
                }
            }));
        }

        member.apply(player -> PreparedMenu.newBuilder()
                .setSize(ChestInventorySize.THREE_ROWS)
                .setTitleGenerator(session -> menuText.getAdaptiveMessage(MenuText.UPGRADE_MENU_TITLE).placeholder("%name%", clan.getName()).toRawText())
                .setFillingStrategy(patternFillingStrategy)
                .create().open(player));
    }
}
