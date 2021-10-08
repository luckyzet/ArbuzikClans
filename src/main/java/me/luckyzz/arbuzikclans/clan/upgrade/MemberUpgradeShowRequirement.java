package me.luckyzz.arbuzikclans.clan.upgrade;

import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.impl.MemberUpgradeData;

public class MemberUpgradeShowRequirement implements UpgradeShowRequirement {

    @Override
    public boolean canShow(ClanUpgrade upgrade, Clan clan) {
        int slotCount = clan.getMembers().getMaxMembers();

        return slotCount < ((MemberUpgradeData) upgrade.getData()).getAmount();
    }
}
