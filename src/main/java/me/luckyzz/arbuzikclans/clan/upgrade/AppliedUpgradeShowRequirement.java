package me.luckyzz.arbuzikclans.clan.upgrade;

import me.luckyzz.arbuzikclans.clan.Clan;

public class AppliedUpgradeShowRequirement implements UpgradeShowRequirement {

    @Override
    public boolean canShow(ClanUpgrade upgrade, Clan clan) {
        return !clan.getUpgrades().getUpgrades().contains(upgrade);
    }
}
