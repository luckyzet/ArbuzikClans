package me.luckyzz.arbuzikclans.clan.upgrade;

import me.luckyzz.arbuzikclans.clan.Clan;

public interface UpgradeShowRequirement {

    boolean canShow(ClanUpgrade upgrade, Clan clan);

}
