package me.luckyzz.arbuzikclans.clan.upgrade;

import me.luckkyyz.luckapi.api.Service;

import java.util.Collection;

public interface ClanUpgradeService extends Service {

    Collection<ClanUpgrade> getUpgrades();

    ClanUpgrade getUpgrade(int index);

}
