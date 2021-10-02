package me.luckyzz.arbuzikclans.clan.upgrade;

import me.luckkyyz.luckapi.api.Service;
import me.luckyzz.arbuzikclans.clan.Clan;

import java.util.Collection;
import java.util.stream.Collectors;

public interface ClanUpgradeService extends Service {

    Collection<ClanUpgrade> getUpgrades();

    default Collection<ClanUpgrade> getUpgradesToShow(Clan clan) {
        return getUpgrades().stream()
                .filter(upgrade -> clan.getUpgrades().getUpgrades().contains(upgrade))
                .collect(Collectors.toList());
    }

    ClanUpgrade getUpgrade(int index);

}
