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
import me.luckkyyz.luckapi.util.inventory.ChestInventorySize;
import me.luckkyyz.luckapi.util.itemstack.ItemBuilders;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberQuest;
import me.luckyzz.arbuzikclans.clan.member.quest.QuestType;
import me.luckyzz.arbuzikclans.config.MenuText;
import me.luckyzz.arbuzikclans.config.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class QuestClanMenu extends AbstractClanMenu {

    QuestClanMenu(MessageConfig<Messages> messageConfig, MessageConfig<MenuText> menuText, ClanMenuService menuService, ChatInputMessageService inputMessageService) {
        super(ClanMenuType.QUESTS, messageConfig, menuText, menuService, inputMessageService);
    }

    @Override
    public void openMenu(ClanMember member, Object... args) {
        BiFunction<MemberQuest, Message, String> placeholders = (quest, message) -> message.toAdaptive()
                .placeholder("%name%", quest.getDisplay())
                .placeholder("%amount%", quest.getNeedCount())
                .placeholder("%need%", quest.getNeedCount() - quest.getCount())
                .placeholder("%state%", quest.isFinished() ? "Выполнен" : quest.getCount() >= 1 ? "В процессе" : "Не выполнен")
                .placeholder("%reward_coins%", quest.getCoins())
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

        int i = 0;
        for (MemberQuest quest : member.getQuests().stream().sorted(Comparator.comparingInt(got -> got.isFinished() ? 1 : 0)).collect(Collectors.toList())) {
            Material material = Material.ZOMBIE_HEAD;

            if (quest.getType() == QuestType.BREAK_BLOCKS) {
                String blockName = quest.getTargetName();
                material = Material.getMaterial(blockName);
            }

            patternFillingStrategy.withButton(symbols[i++], new MenuButton(ItemBuilders.newBuilder()
                    .setType(material)
                    .setDisplay(placeholders.apply(quest, menuText.getMessage(MenuText.QUEST_MENU_ITEM_NAME)))
                    .setLore(Arrays.asList(placeholders.apply(quest, menuText.getMessage(MenuText.QUEST_MENU_ITEM_LORE)).split("\n")))
                    .create(), new ClickCallback() {
                @Override
                public void processClick(Player player, ClickType ignored1, int ignored2) {
                }
            }));
        }

        member.apply(player -> PreparedMenu.newBuilder()
                .setSize(ChestInventorySize.THREE_ROWS)
                .setTitleGenerator(session -> menuText.getMessage(MenuText.QUEST_MENU_TITLE).toRawText())
                .setFillingStrategy(patternFillingStrategy)
                .create().open(player));
    }
}
