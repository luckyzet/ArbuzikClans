package me.luckyzz.arbuzikclans.clan.upgrade;

import me.luckkyyz.luckapi.api.Typable;
import me.luckyzz.arbuzikclans.clan.Clan;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public interface ClanUpgrade extends Typable<UpgradeType> {

    int getIndex();

    ItemStack getItem();

    <T extends UpgradeData> T getData();

    Collection<UpgradeRequirement> getRequirements();

    Collection<UpgradeShowRequirement> getShowRequirements();

    default boolean canBeShow(Clan clan) {
        return getShowRequirements().stream().allMatch(upgradeShowRequirement -> upgradeShowRequirement.canShow(this, clan));
    }

    default boolean isApplicable(Clan clan) {
        return getRequirements().stream().allMatch(upgradeRequirement -> upgradeRequirement.isApplicable(clan));
    }

    void applyUpgrade(Clan clan);

    void removeUpgrade(Clan clan);

}
