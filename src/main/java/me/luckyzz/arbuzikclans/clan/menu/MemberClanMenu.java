package me.luckyzz.arbuzikclans.clan.menu;

import me.luckkyyz.luckapi.chat.input.ChatInputMessageService;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.menu.MenuSession;
import me.luckkyyz.luckapi.menu.PreparedMenu;
import me.luckkyyz.luckapi.menu.button.ClickCallback;
import me.luckkyyz.luckapi.menu.button.MenuButton;
import me.luckkyyz.luckapi.menu.filling.FillingStrategy;
import me.luckkyyz.luckapi.menu.filling.PatternFillingStrategy;
import me.luckkyyz.luckapi.message.Message;
import me.luckkyyz.luckapi.util.date.FormatDate;
import me.luckkyyz.luckapi.util.inventory.ChestInventorySize;
import me.luckkyyz.luckapi.util.itemstack.ItemBuilders;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.ClanMembers;
import me.luckyzz.arbuzikclans.config.MenuText;
import me.luckyzz.arbuzikclans.config.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class MemberClanMenu extends AbstractClanMenu {

    MemberClanMenu(MessageConfig<Messages> messageConfig, MessageConfig<MenuText> menuText, ClanMenuService menuService) {
        super(ClanMenuType.MEMBERS, messageConfig, menuText, menuService);
    }

    @Override
    public void openMenu(ClanMember member, Object... args) {
        Clan clan = member.getClan();
        ClanMembers members = clan.getMembers();

        BiFunction<ClanMember, Message, String> placeholders = (member1, message) -> message.toAdaptive()
                .placeholder("%name%", member1.getName())
                .placeholder("%rank%", member1.getRank().getPrefix())
                .placeholder("%index%", member1.getRank().getIndex())
                .placeholder("%online%", member1.isOnline() ? "Онлайн" : member1.getLastJoinTime(FormatDate.DATE_TIME))
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

        Comparator<ClanMember> comparator = Comparator.comparingInt(member1 -> member1.getRank().getIndex());

        int i = 0;
        for (ClanMember current : members.getAllMembers().stream().sorted(comparator.reversed()).collect(Collectors.toList())) {
            patternFillingStrategy.withButton(symbols[i++], new MenuButton(ItemBuilders.newBuilder()
                    .setType(Material.PLAYER_HEAD)
                    .setDisplay(placeholders.apply(current, menuText.getMessage(MenuText.MEMBER_MENU_HEAD_NAME)))
                    .setLore(Arrays.asList(placeholders.apply(current, menuText.getMessage(MenuText.MEMBER_MENU_HEAD_LORE)).split("\n")))
                    .create(), new ClickCallback() {
                @Override
                public void processClick(Player player, ClickType type, int ignored) {
                    if(current.getName().equals(member.getName())) {
                        messageConfig.getMessage(Messages.YOURSELF).send(player);
                        return;
                    }
                    if (type.isRightClick()) {
                        menuService.getMenu(ClanMenuType.CONFIRM_KICK, current, member).openMenu(member);
                        return;
                    }
                    if (type.isLeftClick()) {
                        menuService.getMenu(ClanMenuType.RANK_GIVE, current).openMenu(member);
                    }
                }
            }));
        }

        member.apply(player -> PreparedMenu.newBuilder()
                .setTitleGenerator(session -> menuText.getMessage(MenuText.MEMBER_MENU_TITLE).toRawText())
                .setSize(ChestInventorySize.THREE_ROWS)
                .setFillingStrategy(patternFillingStrategy)
                .create().open(player));
    }
}
