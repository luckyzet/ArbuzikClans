package me.luckyzz.arbuzikclans.clan.upgrade;

import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;

import java.util.Collection;

public interface ClanUpgrades {

    Clan getClan();

    Collection<ClanUpgrade> getUpgrades();

    void addUpgrade(ClanUpgrade upgrade, ClanMember member);

    void removeUpgradeSilently(ClanUpgrade upgrade);

}
