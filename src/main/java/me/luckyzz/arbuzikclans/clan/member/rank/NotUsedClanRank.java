package me.luckyzz.arbuzikclans.clan.member.rank;

public interface NotUsedClanRank {

    int getIndex();

    String getPrefix();

    RankPossibilities getPossibilities();

    default boolean hasPossibility(RankPossibility possibility) {
        return getPossibilities().hasPossibility(possibility) || getPossibilities().hasPossibility(RankPossibility.ALL);
    }

    RankRole getRole();

    ClanRank toUsing();

}
