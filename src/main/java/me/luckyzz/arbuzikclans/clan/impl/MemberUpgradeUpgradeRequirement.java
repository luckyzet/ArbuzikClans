package me.luckyzz.arbuzikclans.clan.impl;

import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.upgrade.ClanUpgrade;
import me.luckyzz.arbuzikclans.clan.upgrade.UpgradeRequirement;

public class MemberUpgradeUpgradeRequirement implements UpgradeRequirement {

    private final MemberUpgradeData upgradeData;

    MemberUpgradeUpgradeRequirement(MemberUpgradeData upgradeData) {
        this.upgradeData = upgradeData;
    }

    public MemberUpgradeData getUpgradeData() {
        return upgradeData;
    }

    @Override
    public RequirementType getType() {
        return RequirementType.MEMBERS;
    }

    @Override
    public boolean isApplicable(Clan clan) {
        ClanUpgrade upgrade = clan.getUpgrades().getUpgrade(MemberUpgradeData.class).orElse(null);
        if (upgrade == null) {
            return upgradeData.getLevel() == 1;
        }
        MemberUpgradeData upgradeDataCurrent = upgrade.getData();

        return upgradeData.getLevel() > upgradeDataCurrent.getLevel();
    }

    @Override
    public void applyRequirement(Clan clan) {
        ClanUpgrade upgrade = clan.getUpgrades().getUpgrade(MemberUpgradeData.class).orElse(null);
        if (upgrade == null) {
            return;
        }
        clan.getUpgrades().removeUpgradeSilently(upgrade);
    }
}
