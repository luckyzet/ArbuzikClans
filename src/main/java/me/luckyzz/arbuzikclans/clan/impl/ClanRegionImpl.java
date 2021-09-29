package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckkyyz.luckapi.util.location.LocationSerializers;
import me.luckkyyz.luckapi.util.math.CuboidRegion;
import me.luckkyyz.luckapi.util.player.PlayerUtils;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.rank.RankPossibility;
import me.luckyzz.arbuzikclans.clan.region.ClanRegion;
import me.luckyzz.arbuzikclans.config.ClanConfig;
import me.luckyzz.arbuzikclans.config.Messages;
import me.luckyzz.arbuzikclans.config.Settings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.stream.Collectors;

class ClanRegionImpl implements ClanRegion {

    static NamespacedKey TAG_KEY;
    private final ClanConfig config;
    private final MessageConfig<Messages> messageConfig;
    private final QueryExecutors executors;
    private final Set<ClanMember> accessChestWhitelist;
    private final Set<ClanMember> accessBlocksWhitelist;
    private Clan clan;
    private CuboidRegion cuboid;
    private Location center;
    private boolean accessChests;
    private boolean accessBlocks;
    ClanRegionImpl(ClanConfig config, MessageConfig<Messages> messageConfig, QueryExecutors executors, Location center, boolean accessChests, boolean accessBlocks, Set<ClanMember> accessChestWhitelist, Set<ClanMember> accessBlocksWhitelist) {
        this.config = config;
        this.messageConfig = messageConfig;
        this.executors = executors;
        this.center = center;
        calculateCuboid();
        this.accessChests = accessChests;
        this.accessBlocks = accessBlocks;
        this.accessChestWhitelist = accessChestWhitelist;
        this.accessBlocksWhitelist = accessBlocksWhitelist;
    }

    static void setTagKey(Plugin plugin) {
        TAG_KEY = new NamespacedKey(plugin, "clanregion");
    }

    @Override
    public Clan getClan() {
        return clan;
    }

    void setClan(Clan clan) {
        this.clan = clan;
    }

    @Override
    public CuboidRegion getCuboid() {
        return cuboid;
    }

    @Override
    public Location getCenterLocation() {
        return center;
    }

    private void calculateCuboid() {
        World world = center.getWorld();
        int size = config.getInt(Settings.REGION_SIZE);

        int minX = center.getBlockX() - size;
        int minY = center.getBlockY() - size;
        int minZ = center.getBlockZ() - size;
        Location minLocation = new Location(world, minX, minY, minZ);

        int maxX = center.getBlockX() + size;
        int maxY = center.getBlockY() + size;
        int maxZ = center.getBlockZ() + size;
        Location maxLocation = new Location(world, maxX, maxY, maxZ);

        cuboid = new CuboidRegion(minLocation, maxLocation);
    }

    @Override
    public boolean setCenterLocation(Location location, ClanMember member) {
        if (!member.hasPossibility(RankPossibility.REGION_CREATE)) {
            member.apply(player -> messageConfig.getMessage(Messages.CLAN_REGION_CANNOT_CREATE).send(player));
            return false;
        }

        this.center = location;
        calculateCuboid();

        executors.async().update("UPDATE clanRegions SET center = ? WHERE clan = ?", LocationSerializers.string().serialize(center), clan.getId());

        member.apply(player -> messageConfig.getMessage(Messages.CLAN_REGION_CREATE_SUCCESS_EXECUTOR).send(player));
        clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_REGION_CREATE_SUCCESS_LOCAL)
                .placeholder("%rank%", member.getRank().getPrefix())
                .placeholder("%name%", member.getName()));

        return true;
    }

    @Override
    public boolean canAccessChest(ClanMember member) {
        if (member.hasPossibility(RankPossibility.REGION_ACCESS)) {
            return true;
        }
        return accessChests || accessChestWhitelist.contains(member);
    }

    @Override
    public boolean canAccessBlocks(ClanMember member) {
        if (member.hasPossibility(RankPossibility.REGION_ACCESS)) {
            return true;
        }
        return accessBlocks || accessBlocksWhitelist.contains(member);
    }

    @Override
    public void setAccessChest(boolean can, ClanMember member) {
        if (accessChests == can) {
            member.apply(player -> messageConfig.getMessage(Messages.SUCCESS_BUT_ALREADY).send(player));
            return;
        }
        accessChests = can;

        executors.async().update("UPDATE clanRegions SET accessChests = ? WHERE clan = ?", accessChests, clan.getId());

        if (accessChests) {
            member.apply(player -> messageConfig.getMessage(Messages.CLAN_REGION_ACCESS_CHESTS_ENABLED_EXECUTOR));
            clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_REGION_ACCESS_CHESTS_ENABLED_LOCAL)
                    .placeholder("%rank%", member.getRank().getPrefix())
                    .placeholder("%name%", member.getName()));
        } else {
            member.apply(player -> messageConfig.getMessage(Messages.CLAN_REGION_ACCESS_CHESTS_DISABLED_EXECUTOR));
            clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_REGION_ACCESS_CHESTS_DISABLED_LOCAL)
                    .placeholder("%rank%", member.getRank().getPrefix())
                    .placeholder("%name%", member.getName()));
        }
    }

    @Override
    public void setAccessBlocks(boolean can, ClanMember member) {
        if (accessBlocks == can) {
            member.apply(player -> messageConfig.getMessage(Messages.SUCCESS_BUT_ALREADY).send(player));
            return;
        }
        accessBlocks = can;

        executors.async().update("UPDATE clanRegions SET accessBlocks = ? WHERE clan = ?", accessBlocks, clan.getId());

        if (accessChests) {
            member.apply(player -> messageConfig.getMessage(Messages.CLAN_REGION_ACCESS_BLOCKS_ENABLED_EXECUTOR));
            clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_REGION_ACCESS_BLOCKS_ENABLED_LOCAL)
                    .placeholder("%rank%", member.getRank().getPrefix())
                    .placeholder("%name%", member.getName()));
        } else {
            member.apply(player -> messageConfig.getMessage(Messages.CLAN_REGION_ACCESS_BLOCKS_DISABLED_EXECUTOR));
            clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_REGION_ACCESS_BLOCKS_DISABLED_LOCAL)
                    .placeholder("%rank%", member.getRank().getPrefix())
                    .placeholder("%name%", member.getName()));
        }
    }

    @Override
    public void setAccessChest(boolean can, ClanMember forMember, ClanMember member) {
        if (accessChestWhitelist.contains(forMember) == can) {
            member.apply(player -> messageConfig.getMessage(Messages.SUCCESS_BUT_ALREADY).send(player));
            return;
        }

        if (can) {
            accessChestWhitelist.add(forMember);
            member.apply(player -> messageConfig.getAdaptiveMessage(Messages.CLAN_REGION_ACCESS_CHESTS_WHITELIST_ADD_EXECUTOR)
                    .placeholder("%rank%", forMember.getRank().getPrefix())
                    .placeholder("%name%", forMember.getName()));
            forMember.apply(player -> messageConfig.getAdaptiveMessage(Messages.CLAN_REGION_ACCESS_CHESTS_WHITELIST_ADD_TARGET)
                    .placeholder("%rank%", member.getRank())
                    .placeholder("%name%", member.getName()));
            clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_REGION_ACCESS_CHESTS_WHITELIST_ADD_TARGET)
                    .placeholder("%rank%", member.getRank())
                    .placeholder("%name%", member.getName())
                    .placeholder("%target_rank%", forMember.getRank().getPrefix())
                    .placeholder("%target_name%", forMember.getName()));
        } else {
            accessChestWhitelist.remove(forMember);
            member.apply(player -> messageConfig.getAdaptiveMessage(Messages.CLAN_REGION_ACCESS_CHESTS_WHITELIST_REMOVE_EXECUTOR)
                    .placeholder("%rank%", forMember.getRank().getPrefix())
                    .placeholder("%name%", forMember.getName()));
            forMember.apply(player -> messageConfig.getAdaptiveMessage(Messages.CLAN_REGION_ACCESS_CHESTS_WHITELIST_REMOVE_TARGET)
                    .placeholder("%rank%", member.getRank())
                    .placeholder("%name%", member.getName()));
            clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_REGION_ACCESS_CHESTS_WHITELIST_REMOVE_TARGET)
                    .placeholder("%rank%", member.getRank())
                    .placeholder("%name%", member.getName())
                    .placeholder("%target_rank%", forMember.getRank().getPrefix())
                    .placeholder("%target_name%", forMember.getName()));
        }

        String whitelistString = accessChestWhitelist.stream().map(ClanMember::getName).collect(Collectors.joining(","));
        executors.async().update("UPDATE clanRegions SET accessChestsWhitelist = ? WHERE clan = ?", whitelistString, clan.getId());
    }

    @Override
    public void setAccessBlocks(boolean can, ClanMember forMember, ClanMember member) {
        if (accessBlocksWhitelist.contains(forMember) == can) {
            member.apply(player -> messageConfig.getMessage(Messages.SUCCESS_BUT_ALREADY).send(player));
            return;
        }

        if (can) {
            accessBlocksWhitelist.add(forMember);
            member.apply(player -> messageConfig.getAdaptiveMessage(Messages.CLAN_REGION_ACCESS_BLOCKS_WHITELIST_ADD_EXECUTOR)
                    .placeholder("%rank%", forMember.getRank().getPrefix())
                    .placeholder("%name%", forMember.getName()));
            forMember.apply(player -> messageConfig.getAdaptiveMessage(Messages.CLAN_REGION_ACCESS_BLOCKS_WHITELIST_ADD_TARGET)
                    .placeholder("%rank%", member.getRank())
                    .placeholder("%name%", member.getName()));
            clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_REGION_ACCESS_BLOCKS_WHITELIST_ADD_TARGET)
                    .placeholder("%rank%", member.getRank())
                    .placeholder("%name%", member.getName())
                    .placeholder("%target_rank%", forMember.getRank().getPrefix())
                    .placeholder("%target_name%", forMember.getName()));
        } else {
            accessBlocksWhitelist.remove(forMember);
            member.apply(player -> messageConfig.getAdaptiveMessage(Messages.CLAN_REGION_ACCESS_BLOCKS_WHITELIST_REMOVE_EXECUTOR)
                    .placeholder("%rank%", forMember.getRank().getPrefix())
                    .placeholder("%name%", forMember.getName()));
            forMember.apply(player -> messageConfig.getAdaptiveMessage(Messages.CLAN_REGION_ACCESS_BLOCKS_WHITELIST_REMOVE_TARGET)
                    .placeholder("%rank%", member.getRank())
                    .placeholder("%name%", member.getName()));
            clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_REGION_ACCESS_BLOCKS_WHITELIST_REMOVE_TARGET)
                    .placeholder("%rank%", member.getRank())
                    .placeholder("%name%", member.getName())
                    .placeholder("%target_rank%", forMember.getRank().getPrefix())
                    .placeholder("%target_name%", forMember.getName()));
        }

        String whitelistString = accessBlocksWhitelist.stream().map(ClanMember::getName).collect(Collectors.joining(","));
        executors.async().update("UPDATE clanRegions SET accessBlocksWhitelist = ? WHERE clan = ?", whitelistString, clan.getId());
    }

    @Override
    public void setAccessChestSilently(boolean can, ClanMember forMember) {
        if (accessChestWhitelist.contains(forMember) == can) {
            return;
        }

        if (can) {
            accessChestWhitelist.add(forMember);
        } else {
            accessChestWhitelist.remove(forMember);
        }

        String whitelistString = accessChestWhitelist.stream().map(ClanMember::getName).collect(Collectors.joining(","));
        executors.async().update("UPDATE clanRegions SET accessChestsWhitelist = ? WHERE clan = ?", whitelistString, clan.getId());
    }

    @Override
    public void setAccessBlocksSilently(boolean can, ClanMember forMember) {
        if (accessBlocksWhitelist.contains(forMember) == can) {
            return;
        }

        if (can) {
            accessBlocksWhitelist.add(forMember);
        } else {
            accessBlocksWhitelist.remove(forMember);
        }

        String whitelistString = accessBlocksWhitelist.stream().map(ClanMember::getName).collect(Collectors.joining(","));
        executors.async().update("UPDATE clanRegions SET accessBlocksWhitelist = ? WHERE clan = ?", whitelistString, clan.getId());
    }

    @Override
    public void giveItem(ClanMember member) {
        if (isRegionExists()) {
            member.apply(player -> messageConfig.getMessage(Messages.CLAN_REGION_EXISTS).send(player));
            return;
        }

        ItemStack itemStack = config.getRegionItem().get();
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(TAG_KEY, PersistentDataType.INTEGER, clan.getId());
            itemStack.setItemMeta(meta);

            member.apply(player -> {
                PlayerUtils.giveItems(player, itemStack);
                messageConfig.getMessage(Messages.CLAN_REGION_ITEM_GAVE).send(player);
            });
        } else {
            member.apply(player -> messageConfig.getMessage(Messages.SOMETHING_WENT_WRONG).send(player));
        }
    }

    @Override
    public void breakRegion(ClanMember member) {
        if (!member.hasPossibility(RankPossibility.REGION_BREAK)) {
            member.apply(player -> messageConfig.getMessage(Messages.NOT_ACCESS).send(player));
            return;
        }
        if (!isRegionExists()) {
            member.apply(player -> messageConfig.getMessage(Messages.CLAN_REGION_NOT_EXISTS).send(player));
            return;
        }
        getCenterBlock().setType(Material.AIR);
        center = null;
        cuboid = null;

        executors.async().update("UPDATE clanRegions SET center = ? WHERE clan = ?", "null", clan.getId());
    }
}
