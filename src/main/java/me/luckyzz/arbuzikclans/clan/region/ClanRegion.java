package me.luckyzz.arbuzikclans.clan.region;

import me.luckkyyz.luckapi.util.math.CuboidRegion;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ClanRegion {

    Clan getClan();

    default boolean isRegionExists() {
        return getCenterLocation() != null && getCuboid() != null;
    }

    CuboidRegion getCuboid();

    Location getCenterLocation();

    CompletableFuture<Boolean> setCenterLocation(Location location, ClanMember member);

    default CompletableFuture<Boolean> setCenterLocation(Block block, ClanMember member) {
        return setCenterLocation(block.getLocation(), member);
    }

    default Block getCenterBlock() {
        return getCenterLocation().getBlock();
    }

    default boolean isInRegion(Location location) {
        return isRegionExists() && getCuboid().contains(location);
    }

    boolean getAccessChest();

    boolean getAccessBlocks();

    List<ClanMember> getAccessChestWhitelist();

    default boolean getAccessChestWhitelist(ClanMember member) {
        return getAccessChestWhitelist().contains(member);
    }

    List<ClanMember> getAccessBlockWhitelist();

    default boolean getAccessBlocksWhitelist(ClanMember member) {
        return getAccessBlockWhitelist().contains(member);
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
