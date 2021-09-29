package me.luckyzz.arbuzikclans.clan.upgrade;

import me.luckyzz.arbuzikclans.clan.Clan;

public interface UpgradeRequirement {

    boolean isApplicable(Clan clan);

    void applyRequirement(Clan clan);

}
