package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRank;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRanks;
import me.luckyzz.arbuzikclans.clan.member.rank.RankRole;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

class ClanRanksImpl implements ClanRanks {

    private final QueryExecutors executors;
    private final Map<Integer, ClanRank> ranks;
    private Clan clan;

    ClanRanksImpl(QueryExecutors executors, Map<Integer, ClanRank> ranks) {
        this.executors = executors;
        this.ranks = ranks;
    }

    void addRank(ClanRank rank) {
        ranks.put(rank.getIndex(), rank);

        if(rank instanceof ClanRankImpl) {
            ((ClanRankImpl) rank).setClan(clan);
        }

        executors.async().update("INSERT INTO clanRanks VALUES (?, ?, ?)", rank.getClan().getId(), rank.getIndex(), rank.getPrefix());
    }

    @Override
    public Clan getClan() {
        return clan;
    }

    void setClan(Clan clan) {
        this.clan = clan;

        ranks.values().forEach(rank -> {
            if(rank instanceof ClanRankImpl) {
                ClanRankImpl clanRank = (ClanRankImpl) rank;
                clanRank.setClan(clan);
            }
        });
    }

    @Override
    public ClanRank getRank(int index) {
        return ranks.get(index);
    }

    @Override
    public Collection<ClanRank> getRanks() {
        return ranks.values();
    }

    @Override
    public ClanRank getRank(RankRole role) {
        return ranks.values().stream()
                .filter(rank -> rank.getRole() == role)
                .findFirst().orElse(null);
    }

}
