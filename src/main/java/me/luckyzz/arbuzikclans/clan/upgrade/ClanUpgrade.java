package me.luckyzz.arbuzikclans.clan.upgrade;

import me.luckyzz.arbuzikclans.clan.Clan;

import java.util.Collection;

public interface ClanUpgrade {

    int getIndex();

    UpgradeType getType();

    <T extends UpgradeData> T getData();

    Collection<UpgradeRequirement> getRequirements();

    default boolean isApplicable(Clan clan) {
        return getRequirements().stream().allMatch(upgradeRequirement -> upgradeRequirement.isApplicable(clan));
    }

    void applyUpgrade(Clan clan);

    void removeUpgrade(Clan clan);

}
