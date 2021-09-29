package me.luckyzz.arbuzikclans.clan.member.rank;

import me.luckyzz.arbuzikclans.clan.Clan;

import java.util.Collection;

public interface ClanRanks {

    Clan getClan();

    ClanRank getRank(int index);

    ClanRank getRank(RankRole role);

    Collection<ClanRank> getRanks();

}
