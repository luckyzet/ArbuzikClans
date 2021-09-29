package me.luckyzz.arbuzikclans.clan.impl;

import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRank;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRanks;
import me.luckyzz.arbuzikclans.clan.member.rank.RankRole;

import java.util.Set;

class ClanRanksImpl implements ClanRanks {

    private final Set<ClanRank> ranks;
    private Clan clan;

    ClanRanksImpl(Set<ClanRank> ranks) {
        this.ranks = ranks;
    }

    @Override
    public Clan getClan() {
        return clan;
    }

    void setClan(Clan clan) {
        this.clan = clan;
    }

    @Override
    public ClanRank getRank(int index) {
        return ranks.stream()
                .filter(rank -> rank.getIndex() == index)
                .findFirst().orElse(null);
    }

    @Override
    public ClanRank getRank(RankRole role) {
        return ranks.stream()
                .filter(rank -> rank.getRole() == role)
                .findFirst().orElse(null);
    }

}
