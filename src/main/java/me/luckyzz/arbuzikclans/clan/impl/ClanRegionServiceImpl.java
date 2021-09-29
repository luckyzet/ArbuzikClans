package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.event.ExtendedListener;
import me.luckkyyz.luckapi.util.permission.PermissionUtil;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.ClanService;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.region.ClanRegion;
import me.luckyzz.arbuzikclans.clan.region.ClanRegionService;
import me.luckyzz.arbuzikclans.config.Messages;
import me.luckyzz.arbuzikclans.util.Permissions;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class ClanRegionServiceImpl extends ExtendedListener implements ClanRegionService {

    private final MessageConfig<Messages> messageConfig;
    private final ClanService clanService;

    public ClanRegionServiceImpl(Plugin plugin, MessageConfig<Messages> messageConfig, ClanService clanService) {
        super(plugin);
        this.messageConfig = messageConfig;
        this.clanService = clanService;

        ClanRegionImpl.setTagKey(plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();
        if(itemStack == null || itemStack.getType() != Material.WOODEN_AXE) {
            return;
        }
        Block block = event.getClickedBlock();
        if(block == null) {
            return;
        }
        Location location = block.getLocation();

        clanService.getAllClans().forEach(clan -> {
            ClanRegion region = clan.getRegion();
            if (region.isRegionExists() && region.isInRegion(location)) {
                event.setCancelled(true);
                messageConfig.getMessage(Messages.NOT_ACCESS).send(player);
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onCommandExpand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().replaceFirst("/", "");

        String[] split = message.split(" ");

        if(split.length != 3) {
            return;
        }

        if(split[0].equalsIgnoreCase("expand") && (!split[2].equalsIgnoreCase("vert") && !split[2].equalsIgnoreCase("up") && !split[2].equalsIgnoreCase("down"))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().replaceFirst("/", "");

        if(!message.equalsIgnoreCase("pos1") && !message.equalsIgnoreCase("pos2")) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
        if(itemStack.getType() != Material.WOODEN_AXE) {
            return;
        }
        Location location = player.getLocation();

        clanService.getAllClans().forEach(clan -> {
            ClanRegion region = clan.getRegion();
            if (region.isRegionExists() && region.isInRegion(location)) {
                event.setCancelled(true);
                messageConfig.getMessage(Messages.NOT_ACCESS).send(player);
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        ItemStack hand = event.getItemInHand();

        ItemMeta meta = hand.getItemMeta();
        if (meta == null) {
            return;
        }
        PersistentDataContainer container = meta.getPersistentDataContainer();
        int id = container.getOrDefault(ClanRegionImpl.TAG_KEY, PersistentDataType.INTEGER, -1);
        if (id == -1) {
            return;
        }
        Clan clan = clanService.getClan(id);
        if (clan == null) {
            return;
        }
        ClanMember member = clan.getMembers().getMember(player);
        if (member == null) {
            return;
        }
        boolean success = clan.getRegion().setCenterLocation(block, member);
        event.setCancelled(!success);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        Location location = block.getLocation();

        if (PermissionUtil.hasPermission(player, Permissions.REGION_BYPASS)) {
            return;
        }

        clanService.getAllClans().forEach(clan -> {
            ClanMember member = clan.getMembers().getMember(player);
            ClanRegion region = clan.getRegion();
            if (region.isRegionExists() && region.isInRegion(location) && (member == null || !region.canAccessBlocks(member))) {
                event.setCancelled(true);
                messageConfig.getMessage(Messages.NOT_ACCESS).send(player);
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location = block.getLocation();

        if (PermissionUtil.hasPermission(player, Permissions.REGION_BYPASS)) {
            return;
        }

        clanService.getAllClans().forEach(clan -> {
            ClanMember member = clan.getMembers().getMember(player);
            ClanRegion region = clan.getRegion();
            if (region.isRegionExists() && region.isInRegion(location) && (member == null || !region.canAccessBlocks(member))) {
                event.setCancelled(true);
                messageConfig.getMessage(Messages.NOT_ACCESS).send(player);
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location = block.getLocation();

        clanService.getAllClans().forEach(clan -> {
            ClanMember member = clan.getMembers().getMember(player);
            ClanRegion region = clan.getRegion();
            if (member != null && region.isRegionExists()) {
                Location center = region.getCenterLocation();
                World centerWorld = center.getWorld();
                World locationWorld = location.getWorld();
                if (centerWorld == null || locationWorld == null) {
                    return;
                }
                if (!centerWorld.getName().equals(locationWorld.getName()) || center.getBlockX() != location.getBlockX() || center.getBlockY() != location.getBlockY() || center.getBlockZ() != location.getBlockZ()) {
                    return;
                }
                event.setCancelled(true);
                event.setDropItems(false);
                event.setExpToDrop(0);
                region.breakRegion(member);
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onEntityInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Location location = event.getRightClicked().getLocation();

        if (PermissionUtil.hasPermission(player, Permissions.REGION_BYPASS)) {
            return;
        }

        clanService.getAllClans().forEach(clan -> {
            ClanMember member = clan.getMembers().getMember(player);
            ClanRegion region = clan.getRegion();
            if (region.isRegionExists() && region.isInRegion(location) && (member == null || !region.canAccessBlocks(member))) {
                event.setCancelled(true);
                messageConfig.getMessage(Messages.NOT_ACCESS).send(player);
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onInteractBlock(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        Material type = block.getType();
        Location location = block.getLocation();

        if (PermissionUtil.hasPermission(player, Permissions.REGION_BYPASS)) {
            return;
        }

        clanService.getAllClans().forEach(clan -> {
            ClanMember member = clan.getMembers().getMember(player);
            ClanRegion region = clan.getRegion();

            if (region.isRegionExists() && region.isInRegion(location)) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (type == Material.CHEST || type == Material.ENDER_CHEST || type == Material.TRAPPED_CHEST)) {
                    if ((member == null || !region.canAccessChest(member))) {
                        event.setCancelled(true);
                        messageConfig.getMessage(Messages.NOT_ACCESS).send(player);
                    }
                    return;
                }

                if ((member == null || !region.canAccessBlocks(member))) {
                    event.setCancelled(true);
                    messageConfig.getMessage(Messages.NOT_ACCESS).send(player);
                }
            }
        });
    }


}
