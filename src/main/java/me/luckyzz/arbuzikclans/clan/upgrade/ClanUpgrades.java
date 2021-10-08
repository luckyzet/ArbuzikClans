package me.luckyzz.arbuzikclans.clan.upgrade;

import me.luckkyyz.luckapi.util.function.Optionality;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;

import java.util.Collection;
import java.util.stream.Collectors;

public interface ClanUpgrades {

    Clan getClan();

    Collection<ClanUpgrade> getUpgrades();

    default Optionality<ClanUpgrade> getUpgrade(Class<? extends UpgradeData> clazz) {
        return Optionality.convert(getUpgrades().stream()
                .filter(upgrade -> upgrade.getData().getClass().equals(clazz))
                .findFirst());
    }

    default Collection<ClanUpgrade> getUpgrades(Class<? extends UpgradeData> clazz) {
        return getUpgrades().stream()
                .filter(upgrade -> upgrade.getData().getClass().equals(clazz))
                .collect(Collectors.toSet());
    }

    void addUpgrade(ClanUpgrade upgrade, ClanMember member);

    void removeUpgradeSilently(ClanUpgrade upgrade);

}
