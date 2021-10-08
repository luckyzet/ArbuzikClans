package me.luckyzz.arbuzikclans.clan.menu;

import me.luckkyyz.luckapi.chat.input.ChatInputMessageService;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.menu.MenuSession;
import me.luckkyyz.luckapi.menu.PreparedMenu;
import me.luckkyyz.luckapi.menu.button.ClickCallback;
import me.luckkyyz.luckapi.menu.button.MenuButton;
import me.luckkyyz.luckapi.menu.filling.PatternFillingStrategy;
import me.luckkyyz.luckapi.message.Message;
import me.luckkyyz.luckapi.util.date.FormatDate;
import me.luckkyyz.luckapi.util.inventory.ChestInventorySize;
import me.luckkyyz.luckapi.util.itemstack.ItemBuilders;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.rank.RankPossibility;
import me.luckyzz.arbuzikclans.config.MenuText;
import me.luckyzz.arbuzikclans.config.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.function.Function;

public class MainClanMenu extends AbstractClanMenu {

    MainClanMenu(MessageConfig<Messages> messageConfig, MessageConfig<MenuText> menuText, ClanMenuService menuService) {
        super(ClanMenuType.MAIN, messageConfig, menuText, menuService);
    }

    @Override
    public void openMenu(ClanMember member, Object... args) {
        Clan clan = member.getClan();

        Function<Message, String> placeholders = message -> message.toAdaptive()
                .placeholder("%name%", clan.getName())
                .placeholder("%date%", clan.getDateCreated(FormatDate.DATE))
                .placeholder("%rank%", member.getRank().getPrefix())
                .placeholder("%id%", clan.getId())
                .placeholder("%members_all%", clan.getMembers().getAllMembers().size())
                .placeholder("%members_max%", clan.getMembers().getMaxMembers())
                .placeholder("%members_online%", clan.getMembers().getAllMembers().stream().mapToInt(member1 -> member1.isOnline() ? 1 : 0).sum())
                .placeholder("%money%", clan.getMoney())
                .placeholder("%coins%", clan.getCoins())
                .toRawText();

        PatternFillingStrategy<MenuSession> patternFillingStrategy = new PatternFillingStrategy<>().setPattern("I---A----", "-E-D-С-B-", "--F-G-J--")
                .withButton('A', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.COMPASS)
                        .setDisplay(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_COMPASS_NAME)))
                        .setLore(Arrays.asList(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_COMPASS_LORE)).split("\n")))
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored2, int ignored3) {
                    }
                })).withButton('B', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.ENDER_PEARL)
                        .setDisplay(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_BASE_NAME)))
                        .setLore(Arrays.asList(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_BASE_LORE)).split("\n")))
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored2, int ignored3) {
                        menuService.getMenu(ClanMenuType.BASE).openMenu(member);
                    }
                })).withButton('С', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.PLAYER_HEAD)
                        .setDisplay(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_HEAD_NAME)))
                        .setLore(Arrays.asList(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_HEAD_LORE)).split("\n")))
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored2, int ignored3) {
                        menuService.getMenu(ClanMenuType.MEMBERS).openMenu(member);
                    }
                })).withButton('D', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.WHEAT_SEEDS)
                        .setDisplay(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_QUEST_NAME)))
                        .setLore(Arrays.asList(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_QUEST_LORE)).split("\n")))
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored2, int ignored3) {
                        menuService.getMenu(ClanMenuType.QUESTS).openMenu(member);
                    }
                })).withButton('E', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.DIAMOND)
                        .setDisplay(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_UPGRADE_NAME)))
                        .setLore(Arrays.asList(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_UPGRADE_LORE)).split("\n")))
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored2, int ignored3) {
                        menuService.getMenu(ClanMenuType.UPGRADE).openMenu(member);
                    }
                })).withButton('F', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.BOW)
                        .setDisplay(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_RANK_NAME)))
                        .setLore(Arrays.asList(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_RANK_LORE)).split("\n")))
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored2, int ignored3) {
                        menuService.getMenu(ClanMenuType.RANK_SHOW).openMenu(member);
                    }
                })).withButton('G', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.PAPER)
                        .setDisplay(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_CHAT_NAME)))
                        .setLore(Arrays.asList(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_CHAT_LORE)).split("\n")))
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored2, int ignored3) {
                        menuService.getMenu(ClanMenuType.CHAT).openMenu(member);
                    }
                })).withButton('J', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.EMERALD)
                        .setDisplay(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_SHOP_NAME)))
                        .setLore(Arrays.asList(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_SHOP_LORE)).split("\n")))
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored2, int ignored3) {
                        menuService.getMenu(ClanMenuType.SHOP).openMenu(member);
                    }
                }));

        if(member.hasPossibility(RankPossibility.DISBAND)) {
            patternFillingStrategy.withButton('I', new MenuButton(ItemBuilders.newBuilder()
                    .setType(Material.BARRIER)
                    .setDisplay(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_BARRIER_NAME)))
                    .setLore(Arrays.asList(placeholders.apply(menuText.getMessage(MenuText.MAIN_MENU_BARRIER_LORE)).split("\n")))
                    .create(), new ClickCallback() {
                @Override
                public void processClick(Player player, ClickType ignored2, int ignored3) {
                    menuService.getMenu(ClanMenuType.CONFIRM_DISBAND, member).openMenu(member);
                }
            }));
        }

        member.apply(player -> PreparedMenu.newBuilder()
                .setSize(ChestInventorySize.THREE_ROWS)
                .setTitleGenerator(session -> menuText.getAdaptiveMessage(MenuText.MAIN_MENU_TITLE).placeholder("%name%", clan.getName()).toRawText())
                .setFillingStrategy(patternFillingStrategy)
                .create().open(player));
    }
}
