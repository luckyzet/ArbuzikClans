package me.luckyzz.arbuzikclans.clan.member.rank;

import me.luckyzz.arbuzikclans.clan.Clan;

public interface NotUsedClanRank {

    int getIndex();

    String getPrefix();

    RankPossibilities getPossibilities();

    default boolean hasPossibility(RankPossibility possibility) {
        return getPossibilities().hasPossibility(possibility);
    }

    RankRole getRole();

    ClanRank toUsing(Clan clan);

}
