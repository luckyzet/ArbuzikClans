package me.luckyzz.arbuzikclans.clan.menu;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.menu.MenuSession;
import me.luckkyyz.luckapi.menu.PreparedMenu;
import me.luckkyyz.luckapi.menu.button.ClickCallback;
import me.luckkyyz.luckapi.menu.button.MenuButton;
import me.luckkyyz.luckapi.menu.filling.PatternFillingStrategy;
import me.luckkyyz.luckapi.message.Message;
import me.luckkyyz.luckapi.provider.economy.EconomicUser;
import me.luckkyyz.luckapi.provider.economy.EconomyProvider;
import me.luckkyyz.luckapi.util.inventory.ChestInventorySize;
import me.luckkyyz.luckapi.util.itemstack.ItemBuilderImpl;
import me.luckkyyz.luckapi.util.itemstack.ItemBuilders;
import me.luckkyyz.luckapi.util.player.PlayerUtils;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.impl.ItemShopUpgradeData;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.upgrade.ClanUpgrade;
import me.luckyzz.arbuzikclans.config.MenuText;
import me.luckyzz.arbuzikclans.config.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ShopClanMenu extends AbstractClanMenu {

    private final EconomyProvider economyProvider;

    ShopClanMenu(MessageConfig<Messages> messageConfig, MessageConfig<MenuText> menuText, ClanMenuService menuService, EconomyProvider economyProvider) {
        super(ClanMenuType.SHOP, messageConfig, menuText, menuService);
        this.economyProvider = economyProvider;
    }

    @Override
    public void openMenu(ClanMember member, Object... args) {
        Clan clan = member.getClan();
        Collection<ClanUpgrade> upgrades = clan.getUpgrades().getUpgrades(ItemShopUpgradeData.class);

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

        Comparator<ClanUpgrade> comparator = Comparator.comparingInt(upgrade -> ((ItemShopUpgradeData) upgrade.getData()).getPrice());

        int i = 0;
        for (ClanUpgrade current : upgrades.stream().sorted(comparator).collect(Collectors.toList())) {
            ItemShopUpgradeData shopUpgradeData = current.getData();

            ItemBuilderImpl itemBuilder = ItemBuilders.from(shopUpgradeData.getItemStack().clone())
                    .addBlankLore();

            if(!shopUpgradeData.isApplyNow()) {
                itemBuilder.addLoreLine("Недоступно для покупки сейчас...");
                patternFillingStrategy.withButton(symbols[i++], new MenuButton(itemBuilder.create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType type, int ignored) {
                    }
                }));
                continue;
            }

            itemBuilder.addLoreLine("Цена: " + shopUpgradeData.getPrice());

            patternFillingStrategy.withButton(symbols[i++], new MenuButton(itemBuilder.create(), new ClickCallback() {
                @Override
                public void processClick(Player player, ClickType type, int ignored) {
                    player.closeInventory();
                    EconomicUser economicUser = economyProvider.getUser(player);
                    if(!economicUser.hasBalance(shopUpgradeData.getPrice())) {
                        messageConfig.getMessage(Messages.NOT_ENOUGH_MONEY).send(player);
                        return;
                    }
                    economicUser.changeBalance(-shopUpgradeData.getPrice());
                    PlayerUtils.giveItems(player, shopUpgradeData.getItemStack());
                }
            }));
        }

        member.apply(player -> PreparedMenu.newBuilder()
                .setTitleGenerator(session -> menuText.getMessage(MenuText.SHOP_MENU_TITLE).toRawText())
                .setSize(ChestInventorySize.THREE_ROWS)
                .setFillingStrategy(patternFillingStrategy)
                .create().open(player));
    }
}
