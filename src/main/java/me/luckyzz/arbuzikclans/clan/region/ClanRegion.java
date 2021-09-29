package me.luckyzz.arbuzikclans.clan.region;

import me.luckkyyz.luckapi.util.math.CuboidRegion;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import org.bukkit.Location;
import org.bukkit.block.Block;

public interface ClanRegion {

    Clan getClan();

    default boolean isRegionExists() {
        return getCenterLocation() != null && getCuboid() != null;
    }

    CuboidRegion getCuboid();

    Location getCenterLocation();

    boolean setCenterLocation(Location location, ClanMember member);

    default boolean setCenterLocation(Block block, ClanMember member) {
        return setCenterLocation(block.getLocation(), member);
    }

    default Block getCenterBlock() {
        return getCenterLocation().getBlock();
    }

    default boolean isInRegion(Location location) {
        return isRegionExists() && getCuboid().contains(location);
    }

    boolean canAccessChest(ClanMember member);

    boolean canAccessBlocks(ClanMember member);

    void setAccessChest(boolean can, ClanMember member);

    void setAccessBlocks(boolean can, ClanMember member);

    void setAccessChest(boolean can, ClanMember forMember, ClanMember member);

    void setAccessBlocks(boolean can, ClanMember forMember, ClanMember member);

    void setAccessChestSilently(boolean can, ClanMember forMember);

    void setAccessBlocksSilently(boolean can, ClanMember forMember);

    void giveItem(ClanMember member);

    void breakRegion(ClanMember member);

}
