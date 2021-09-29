package me.luckyzz.arbuzikclans.clan.impl;

import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRank;
import me.luckyzz.arbuzikclans.clan.member.rank.NotUsedClanRank;
import me.luckyzz.arbuzikclans.clan.member.rank.RankPossibilities;
import me.luckyzz.arbuzikclans.clan.member.rank.RankRole;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

class NotUsedClanRankImpl implements NotUsedClanRank {

    private final int index;
    private final RankPossibilities possibilities;
    private final RankRole role;
    private String prefix;

    NotUsedClanRankImpl(int index, String prefix, RankPossibilities possibilities, RankRole role) {
        this.index = index;
        this.prefix = prefix;
        this.possibilities = possibilities;
        this.role = role;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public RankPossibilities getPossibilities() {
        return possibilities;
    }

    @Override
    public RankRole getRole() {
        return role;
    }

    @Override
    public ClanRank toUsing(Clan clan) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotUsedClanRankImpl that = (NotUsedClanRankImpl) o;
        return new EqualsBuilder()
                .append(index, that.index)
                .append(prefix, that.prefix)
                .append(possibilities, that.possibilities)
                .append(role, that.role)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(index)
                .append(prefix)
                .append(possibilities)
                .append(role)
                .toHashCode();
    }
}
