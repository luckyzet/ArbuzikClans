package me.luckyzz.arbuzikclans.clan.menu;

import me.luckkyyz.luckapi.chat.input.ChatInputMessageService;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.menu.MenuSession;
import me.luckkyyz.luckapi.menu.PreparedMenu;
import me.luckkyyz.luckapi.menu.button.ClickCallback;
import me.luckkyyz.luckapi.menu.button.MenuButton;
import me.luckkyyz.luckapi.menu.filling.PatternFillingStrategy;
import me.luckkyyz.luckapi.message.AdaptiveMessage;
import me.luckkyyz.luckapi.message.Message;
import me.luckkyyz.luckapi.util.inventory.ChestInventorySize;
import me.luckkyyz.luckapi.util.itemstack.ItemBuilderImpl;
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
import java.util.function.BiFunction;
import java.util.function.Function;

public class ChatClanMenu extends AbstractClanMenu {

    ChatClanMenu(MessageConfig<Messages> messageConfig, MessageConfig<MenuText> menuText, ClanMenuService menuService) {
        super(ClanMenuType.CHAT, messageConfig, menuText, menuService);
    }

    @Override
    public void openMenu(ClanMember member, Object... args) {
        Clan clan = member.getClan();

        PatternFillingStrategy<MenuSession> patternFillingStrategy = new PatternFillingStrategy<>().setPattern("---------", "A--B--C--", "---------")
                .withButton('A', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.CLAY_BALL)
                        .setDisplay(menuText.getMessage(MenuText.BACK_BUTTON_NAME).toRawText())
                        .setLore(Arrays.asList(menuText.getMessage(MenuText.BACK_BUTTON_LORE).toRawText().split("\n")))
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored1, int ignored2) {
                        menuService.getMenu(ClanMenuType.MAIN).openMenu(member);
                    }
                }));

        Function<Message, AdaptiveMessage> placeholders = message -> message.toAdaptive()
                .placeholder("%state%", clan.getChat().isChatEnabled() ? "Да" : "Нет");

        if(member.hasPossibility(RankPossibility.CHAT_ENABLE)) {
            patternFillingStrategy.withButton('B', new MenuButton(ItemBuilders.newBuilder()
                    .setType(Material.COMPASS)
                    .setDisplay(placeholders.apply(menuText.getAdaptiveMessage(MenuText.CHAT_MENU_COMPASS_MANAGE_NAME)).toRawText())
                    .setLore(Arrays.asList(placeholders.apply(menuText.getAdaptiveMessage(MenuText.CHAT_MENU_COMPASS_MANAGE_LORE)).toRawText().split("\n")))
                    .create(), new ClickCallback() {
                @Override
                public void processClick(Player player, ClickType ignored1, int ignored2) {
                    clan.getChat().setChatEnabled(!clan.getChat().isChatEnabled(), member);
                    player.closeInventory();
                }
            }));
        } else {
            patternFillingStrategy.withButton('B', new MenuButton(ItemBuilders.newBuilder()
                    .setType(Material.COMPASS)
                    .setDisplay(placeholders.apply(menuText.getAdaptiveMessage(MenuText.CHAT_MENU_COMPASS_NAME)).toRawText())
                    .setLore(Arrays.asList(placeholders.apply(menuText.getAdaptiveMessage(MenuText.CHAT_MENU_COMPASS_LORE)).toRawText().split("\n")))
                    .create(), new ClickCallback() {
                @Override
                public void processClick(Player player, ClickType ignored1, int ignored2) {
                }
            }));
        }

        ItemBuilderImpl itemBuilder = ItemBuilders.newBuilder()
                .setType(Material.ENDER_PEARL)
                .setDisplay(menuText.getMessage(MenuText.CHAT_MENU_ENDER_PEARL_NAME).toRawText())
                .setLore(Arrays.asList(menuText.getMessage(MenuText.CHAT_MENU_ENDER_PEARL_LORE).toRawText().split("\n")));
        clan.getChatMutes().getMutes().forEach(mute -> {
            if(mute.checkActual()) {
                return;
            }
            itemBuilder.addLoreLine(mute.getWhoMute().getName() + " замутил " + mute.getMuted().getName() + " по причине " + mute.getReason());
        });
        patternFillingStrategy.withButton('C', new MenuButton(itemBuilder.create(), new ClickCallback() {
            @Override
            public void processClick(Player player, ClickType ignored1, int ignored2) {
            }
        }));

        member.apply(player -> PreparedMenu.newBuilder()
                .setSize(ChestInventorySize.THREE_ROWS)
                .setTitleGenerator(session -> menuText.getAdaptiveMessage(MenuText.CHAT_MENU_TITLE).placeholder("%name%", clan.getName()).toRawText())
                .setFillingStrategy(patternFillingStrategy)
                .create().open(player));
    }
}
