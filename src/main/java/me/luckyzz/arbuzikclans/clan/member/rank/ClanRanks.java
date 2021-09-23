package me.luckyzz.arbuzikclans.clan.member.rank;

import me.luckyzz.arbuzikclans.clan.Clan;

public interface ClanRanks {

    Clan getClan();

    ClanRank getRank(int index);

    ClanRank getRank(RankRole role);

}
