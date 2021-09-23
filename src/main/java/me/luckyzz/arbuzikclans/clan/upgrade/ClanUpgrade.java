package me.luckyzz.arbuzikclans.clan.upgrade;

import me.luckyzz.arbuzikclans.clan.Clan;

public interface ClanUpgrade {

    int getIndex();

    UpgradeType getType();

    <T extends UpgradeData> T getData();

}
