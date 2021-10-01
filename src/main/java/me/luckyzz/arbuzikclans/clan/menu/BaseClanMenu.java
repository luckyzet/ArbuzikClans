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
import me.luckyzz.arbuzikclans.clan.member.rank.RankPossibility;
import me.luckyzz.arbuzikclans.clan.region.ClanRegion;
import me.luckyzz.arbuzikclans.config.MenuText;
import me.luckyzz.arbuzikclans.config.Messages;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class BaseClanMenu extends AbstractClanMenu {

    BaseClanMenu(MessageConfig<Messages> messageConfig, MessageConfig<MenuText> menuText, ClanMenuService menuService, ChatInputMessageService inputMessageService) {
        super(ClanMenuType.BASE, messageConfig, menuText, menuService, inputMessageService);
    }

    @Override
    public void openMenu(ClanMember member, Object... args) {
        Clan clan = member.getClan();

        ClanRegion region = clan.getRegion();
        Function<World, String> worldFunction = world -> {
            if (world.getEnvironment() == World.Environment.NORMAL) {
                return "Обычный мир";
            }
            if (world.getEnvironment() == World.Environment.NETHER) {
                return "Ад";
            }
            if (world.getEnvironment() == World.Environment.THE_END) {
                return "Энд";
            }
            return "Кастомный";
        };
        Function<Location, String> locationFunction = location -> location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ();
        Function<List<ClanMember>, String> whitelistFunction = members -> {
            StringBuilder builder = new StringBuilder();

            for (int x = 0; x < members.size(); x++) {
                ClanMember member1 = members.get(x);
                if (member1 == null) {
                    continue;
                }

                builder.append(member1.getName());
                if (x != (members.size() - 1)) {
                    builder.append(",");
                }

                x++;
            }

            if (builder.toString().equals("")) {
                return "Нет";
            }

            return builder.toString();
        };
        Function<Message, String> placeholdersInfo = message -> message.toAdaptive()
                .placeholder("%name%", clan.getName())
                .placeholder("%state%", region.isRegionExists() ? "Существует" : "Отсутствует")
                .placeholder("%world%", region.isRegionExists() ? worldFunction.apply(region.getCenterLocation().getWorld()) : "Нет")
                .placeholder("%center%", region.isRegionExists() ? locationFunction.apply(region.getCenterLocation()) : "Нет")
                .placeholder("%access_chest%", region.isRegionExists() ? region.canAccessChest(member) ? "Да" : "Нет" : "Нет")
                .placeholder("%access_blocks%", region.isRegionExists() ? region.canAccessBlocks(member) ? "Да" : "Нет" : "Нет")
                .placeholder("%can_teleport%", region.isRegionExists() ? "Да" : "Нет")
                .placeholder("%state_blocks%", region.isRegionExists() ? region.getAccessBlocks() ? "Да" : "Нет" : "Нет")
                .placeholder("%state_chest%", region.isRegionExists() ? region.getAccessChest() ? "Да" : "Нет" : "Нет")
                .placeholder("%blocks_whitelist%", region.isRegionExists() ? whitelistFunction.apply(region.getAccessBlockWhitelist()) : "Нет")
                .placeholder("%chests_whitelist%", region.isRegionExists() ? whitelistFunction.apply(region.getAccessChestWhitelist()) : "Нет")
                .toRawText();

        PatternFillingStrategy<MenuSession> patternFillingStrategy = new PatternFillingStrategy<>();
        patternFillingStrategy.withButton('A', new MenuButton(ItemBuilders.newBuilder()
                .setType(Material.CLAY_BALL)
                .setDisplay(menuText.getMessage(MenuText.BACK_BUTTON_NAME).toRawText())
                .setLore(Arrays.asList(menuText.getMessage(MenuText.BACK_BUTTON_LORE).toRawText().split("\n")))
                .create(), new ClickCallback() {
            @Override
            public void processClick(Player player, ClickType ignored1, int ignored2) {
                menuService.getMenu(ClanMenuType.MAIN).openMenu(member);
            }
        }));
        patternFillingStrategy.withButton('B', new MenuButton(ItemBuilders.newBuilder()
                .setType(Material.COMPASS)
                .setDisplay(menuText.getMessage(MenuText.BASE_MENU_COMPASS_NAME).toRawText())
                .setLore(Arrays.asList(placeholdersInfo.apply(menuText.getMessage(MenuText.BASE_MENU_COMPASS_LORE)).split("\n")))
                .create(), new ClickCallback() {
            @Override
            public void processClick(Player player, ClickType ignored1, int ignored2) {
            }
        }));
        patternFillingStrategy.withButton('C', new MenuButton(ItemBuilders.newBuilder()
                .setType(Material.GOLD_INGOT)
                .setDisplay(menuText.getMessage(MenuText.BASE_MENU_GOLD_NAME).toRawText())
                .setLore(Arrays.asList(placeholdersInfo.apply(menuText.getMessage(MenuText.BASE_MENU_GOLD_LORE)).split("\n")))
                .create(), new ClickCallback() {
            @Override
            public void processClick(Player player, ClickType ignored1, int ignored2) {
            }
        }));
        patternFillingStrategy.withButton('D', new MenuButton(ItemBuilders.newBuilder()
                .setType(Material.ENDER_PEARL)
                .setDisplay(menuText.getMessage(MenuText.BASE_MENU_ENDER_PEARL_NAME).toRawText())
                .setLore(Arrays.asList(placeholdersInfo.apply(menuText.getMessage(MenuText.BASE_MENU_ENDER_PEARL_LORE)).split("\n")))
                .create(), new ClickCallback() {
            @Override
            public void processClick(Player player, ClickType ignored1, int ignored2) {
                player.closeInventory();

                if (region.isRegionExists()) {
                    Location center = region.getCenterLocation().clone().add(0, 1, 0);
                    if (center.getBlock().getType() != Material.AIR) {
                        messageConfig.getMessage(Messages.CLAN_REGION_TELEPORT_FAILED_DANGEROUS).send(player);
                        return;
                    }

                    player.teleport(center);
                    messageConfig.getMessage(Messages.CLAN_REGION_TELEPORT_SUCCESS).send(player);
                    return;
                }
                messageConfig.getMessage(Messages.CLAN_REGION_TELEPORT_FAILED).send(player);
            }
        }));
        if (member.hasPossibility(RankPossibility.REGION_MANAGE)) {
            patternFillingStrategy.withButton('E', new MenuButton(ItemBuilders.newBuilder()
                    .setType(Material.STONE)
                    .setDisplay(menuText.getMessage(MenuText.BASE_MENU_STONE_NAME).toRawText())
                    .setLore(Arrays.asList(placeholdersInfo.apply(menuText.getMessage(MenuText.BASE_MENU_STONE_LORE)).split("\n")))
                    .create(), new ClickCallback() {
                @Override
                public void processClick(Player player, ClickType ignored1, int ignored2) {
                    region.setAccessBlocks(!region.getAccessBlocks(), member);
                    player.closeInventory();
                }
            }));

            patternFillingStrategy.withButton('F', new MenuButton(ItemBuilders.newBuilder()
                    .setType(Material.CHEST)
                    .setDisplay(menuText.getMessage(MenuText.BASE_MENU_CHEST_NAME).toRawText())
                    .setLore(Arrays.asList(placeholdersInfo.apply(menuText.getMessage(MenuText.BASE_MENU_CHEST_LORE)).split("\n")))
                    .create(), new ClickCallback() {
                @Override
                public void processClick(Player player, ClickType ignored1, int ignored2) {
                    region.setAccessChest(!region.getAccessChest(), member);
                    player.closeInventory();
                }
            }));

            patternFillingStrategy.withButton('G', new MenuButton(ItemBuilders.newBuilder()
                    .setType(Material.FEATHER)
                    .setDisplay(menuText.getMessage(MenuText.BASE_MENU_FEATHER_NAME).toRawText())
                    .setLore(Arrays.asList(placeholdersInfo.apply(menuText.getMessage(MenuText.BASE_MENU_FEATHER_LORE)).split("\n")))
                    .create(), new ClickCallback() {
                @Override
                public void processClick(Player player, ClickType ignored1, int ignored2) {
                }
            }));

            patternFillingStrategy.withButton('I', new MenuButton(ItemBuilders.newBuilder()
                    .setType(Material.FLINT)
                    .setDisplay(menuText.getMessage(MenuText.BASE_MENU_FLINT_NAME).toRawText())
                    .setLore(Arrays.asList(placeholdersInfo.apply(menuText.getMessage(MenuText.BASE_MENU_FLINT_LORE)).split("\n")))
                    .create(), new ClickCallback() {
                @Override
                public void processClick(Player player, ClickType ignored1, int ignored2) {
                }
            }));

            if(!region.isRegionExists()) {
                patternFillingStrategy.withButton('H', new MenuButton(ItemBuilders.newBuilder()
                        .setType(Material.STICK)
                        .setDisplay(menuText.getMessage(MenuText.BASE_MENU_STICK_NAME).toRawText())
                        .setLore(Arrays.asList(placeholdersInfo.apply(menuText.getMessage(MenuText.BASE_MENU_STICK_LORE)).split("\n")))
                        .create(), new ClickCallback() {
                    @Override
                    public void processClick(Player player, ClickType ignored1, int ignored2) {
                        player.closeInventory();
                        region.giveItem(member);
                    }
                }));

                patternFillingStrategy.setPattern("A-B--C--D", "---------", "H-E-F-G-I");
            } else {
                patternFillingStrategy.setPattern("--B--C--D", "A--------", "--E-F-G-I");
            }
        } else {
            patternFillingStrategy.setPattern("A--------", "--B--C--D", "---------");
        }

        member.apply(player -> PreparedMenu.newBuilder()
                .setTitleGenerator(session -> menuText.getMessage(MenuText.BASE_MENU_TITLE).toRawText())
                .setSize(ChestInventorySize.THREE_ROWS)
                .setFillingStrategy(patternFillingStrategy)
                .create().open(player));
    }
}
