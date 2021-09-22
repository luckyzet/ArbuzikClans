package me.luckyzz.arbuzikclans.clan.rank;

import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;

public interface ClanRank {

    Clan getClan();

    int getIndex();

    String getPrefix();

    void setPrefix(String prefix, ClanMember member);

    boolean hasPossibility(RankPossibility possibility);

}
