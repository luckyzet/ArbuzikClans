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
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRank;
import me.luckyzz.arbuzikclans.config.MenuText;
import me.luckyzz.arbuzikclans.config.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class RankClanMenu extends AbstractClanMenu {

    private final BiConsumer<ClanRank, ClanMember> actionClick;
    private final boolean backButton;

    RankClanMenu(ClanMenuType type, MessageConfig<Messages> messageConfig, MessageConfig<MenuText> menuText, ClanMenuService menuService,
                 BiConsumer<ClanRank, ClanMember> actionClick) {
        super(type, messageConfig, menuText, menuService);
        this.backButton = false;
        this.actionClick = actionClick;
    }

    RankClanMenu(ClanMenuType type, MessageConfig<Messages> messageConfig, MessageConfig<MenuText> menuText, ClanMenuService menuService,
                 boolean backButton, BiConsumer<ClanRank, ClanMember> actionClick) {
        super(type, messageConfig, menuText, menuService);
        this.backButton = backButton;
        this.actionClick = actionClick;
    }

    @Override
    public void openMenu(ClanMember member, Object... args) {
        Clan clan = member.getClan();

        BiFunction<ClanRank, Message, String> placeholders = (rank, message) -> message.toAdaptive()
                .placeholder("%prefix%", rank.getPrefix())
                .toRawText();

        PatternFillingStrategy<MenuSession> patternFillingStrategy = new PatternFillingStrategy<>().setPattern("P--------", "-ABCDEFI-", "---------")
                .withButton('A', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.COAL)
                        .setDisplay(placeholders.apply(clan.getRanks().getRank(1), menuText.getMessage(MenuText.RANK_MENU_COAL_NAME)))
                        .setLore(Arrays.asList(placeholders.apply(clan.getRanks().getRank(1),
                                menuText.getMessage(menuType == ClanMenuType.RANK_GIVE ? MenuText.RANK_MENU_COAL_GIVE_LORE : MenuText.RANK_MENU_COAL_CHANGE_LORE)).split("\n")))
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored1, int ignored2) {
                        actionClick.accept(clan.getRanks().getRank(1), member);
                        player.closeInventory();
                    }
                })).withButton('B', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.IRON_INGOT)
                        .setDisplay(placeholders.apply(clan.getRanks().getRank(2), menuText.getMessage(MenuText.RANK_MENU_IRON_NAME)))
                        .setLore(Arrays.asList(placeholders.apply(clan.getRanks().getRank(2),
                                menuText.getMessage(menuType == ClanMenuType.RANK_GIVE ? MenuText.RANK_MENU_IRON_GIVE_LORE : MenuText.RANK_MENU_IRON_CHANGE_LORE)).split("\n")))
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored1, int ignored2) {
                        actionClick.accept(clan.getRanks().getRank(2), member);
                        player.closeInventory();
                    }
                })).withButton('C', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.REDSTONE)
                        .setDisplay(placeholders.apply(clan.getRanks().getRank(3), menuText.getMessage(MenuText.RANK_MENU_REDSTONE_NAME)))
                        .setLore(Arrays.asList(placeholders.apply(clan.getRanks().getRank(3),
                                menuText.getMessage(menuType == ClanMenuType.RANK_GIVE ? MenuText.RANK_MENU_REDSTONE_GIVE_LORE : MenuText.RANK_MENU_REDSTONE_CHANGE_LORE)).split("\n")))
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored1, int ignored2) {
                        actionClick.accept(clan.getRanks().getRank(3), member);
                        player.closeInventory();
                    }
                })).withButton('D', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.GOLD_INGOT)
                        .setDisplay(placeholders.apply(clan.getRanks().getRank(4), menuText.getMessage(MenuText.RANK_MENU_GOLD_NAME)))
                        .setLore(Arrays.asList(placeholders.apply(clan.getRanks().getRank(4),
                                menuText.getMessage(menuType == ClanMenuType.RANK_GIVE ? MenuText.RANK_MENU_GOLD_GIVE_LORE : MenuText.RANK_MENU_GOLD_CHANGE_LORE)).split("\n")))
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored1, int ignored2) {
                        actionClick.accept(clan.getRanks().getRank(4), member);
                        player.closeInventory();
                    }
                })).withButton('E', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.LAPIS_LAZULI)
                        .setDisplay(placeholders.apply(clan.getRanks().getRank(5), menuText.getMessage(MenuText.RANK_MENU_LAPIS_NAME)))
                        .setLore(Arrays.asList(placeholders.apply(clan.getRanks().getRank(5),
                                menuText.getMessage(menuType == ClanMenuType.RANK_GIVE ? MenuText.RANK_MENU_LAPIS_GIVE_LORE : MenuText.RANK_MENU_LAPIS_CHANGE_LORE)).split("\n")))
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored1, int ignored2) {
                        actionClick.accept(clan.getRanks().getRank(5), member);
                        player.closeInventory();
                    }
                })).withButton('F', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.DIAMOND)
                        .setDisplay(placeholders.apply(clan.getRanks().getRank(6), menuText.getMessage(MenuText.RANK_MENU_DIAMOND_NAME)))
                        .setLore(Arrays.asList(placeholders.apply(clan.getRanks().getRank(6),
                                menuText.getMessage(menuType == ClanMenuType.RANK_GIVE ? MenuText.RANK_MENU_DIAMOND_GIVE_LORE : MenuText.RANK_MENU_DIAMOND_CHANGE_LORE)).split("\n")))
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored1, int ignored2) {
                        actionClick.accept(clan.getRanks().getRank(6), member);
                        player.closeInventory();
                    }
                })).withButton('I', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.EMERALD)
                        .setDisplay(placeholders.apply(clan.getRanks().getRank(7), menuText.getMessage(MenuText.RANK_MENU_EMERALD_NAME)))
                        .setLore(Arrays.asList(placeholders.apply(clan.getRanks().getRank(7),
                                menuText.getMessage(menuType == ClanMenuType.RANK_GIVE ? MenuText.RANK_MENU_EMERALD_GIVE_LORE : MenuText.RANK_MENU_EMERALD_CHANGE_LORE)).split("\n")))
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored1, int ignored2) {
                        actionClick.accept(clan.getRanks().getRank(7), member);
                        player.closeInventory();
                    }
                }));

        if(backButton) {
            patternFillingStrategy.withButton('P', new MenuButton(ItemBuilders.newBuilder()
                    .setType(Material.CLAY_BALL)
                    .setDisplay(menuText.getMessage(MenuText.BACK_BUTTON_NAME).toRawText())
                    .setLore(Arrays.asList(menuText.getMessage(MenuText.BACK_BUTTON_LORE).toRawText().split("\n")))
                    .create(), new ClickCallback() {
                @Override
                public void processClick(Player player, ClickType ignored1, int ignored2) {
                    menuService.getMenu(ClanMenuType.MAIN).openMenu(member);
                }
            }));
        }

        member.apply(player -> PreparedMenu.newBuilder()
                .setTitleGenerator(session -> menuText.getMessage(menuType == ClanMenuType.RANK_GIVE ? MenuText.RANK_MENU_GIVE_TITLE : MenuText.RANK_MENU_CHANGE_TITLE).toRawText())
                .setSize(ChestInventorySize.THREE_ROWS)
                .setFillingStrategy(patternFillingStrategy)
                .create().open(player));
    }
}
