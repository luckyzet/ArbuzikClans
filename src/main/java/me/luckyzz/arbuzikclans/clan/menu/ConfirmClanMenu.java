package me.luckyzz.arbuzikclans.clan.menu;

import me.luckkyyz.luckapi.chat.input.ChatInputMessageService;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.menu.MenuSession;
import me.luckkyyz.luckapi.menu.PreparedMenu;
import me.luckkyyz.luckapi.menu.button.ClickCallback;
import me.luckkyyz.luckapi.menu.button.MenuButton;
import me.luckkyyz.luckapi.menu.filling.PatternFillingStrategy;
import me.luckkyyz.luckapi.util.inventory.ChestInventorySize;
import me.luckkyyz.luckapi.util.itemstack.ItemBuilders;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.config.MenuText;
import me.luckyzz.arbuzikclans.config.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ConfirmClanMenu extends AbstractClanMenu {

    private final Runnable action, actionIfFalse;

    ConfirmClanMenu(ClanMenuType menuType, MessageConfig<Messages> messageConfig, MessageConfig<MenuText> menuText, ClanMenuService menuService, ChatInputMessageService inputMessageService, Runnable action, Runnable actionIfFalse) {
        super(menuType, messageConfig, menuText, menuService, inputMessageService);
        this.action = action;
        this.actionIfFalse = actionIfFalse;
    }

    @Override
    public void openMenu(ClanMember member, Object... args) {
        PatternFillingStrategy<MenuSession> patternFillingStrategy = new PatternFillingStrategy<>()
                .setPattern("---------", "---A-B---", "---------")
                .withButton('A', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.GREEN_WOOL)
                        .setDisplay(menuText.getMessage(MenuText.CONFIRM_MENU_YES_NAME).toRawText())
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored1, int ignored2) {
                        action.run();
                        player.closeInventory();
                    }
                })).withButton('B', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.RED_WOOL)
                        .setDisplay(menuText.getMessage(MenuText.CONFIRM_MENU_NO_NAME).toRawText())
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored1, int ignored2) {
                        actionIfFalse.run();
                        player.closeInventory();
                    }
                }));

        member.apply(player -> PreparedMenu.newBuilder()
                .setTitleGenerator(session -> menuText.getMessage(MenuText.CONFIRM_MENU_TITLE).toRawText())
                .setSize(ChestInventorySize.THREE_ROWS)
                .setFillingStrategy(patternFillingStrategy)
                .create().open(player));
    }
}

