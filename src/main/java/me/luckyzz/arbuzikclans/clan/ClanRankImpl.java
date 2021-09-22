package me.luckyzz.arbuzikclans.clan;

import me.luckkyyz.luckapi.util.color.ColorUtils;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.rank.ClanRank;
import me.luckyzz.arbuzikclans.clan.rank.RankPossibilities;
import me.luckyzz.arbuzikclans.clan.rank.RankPossibility;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

class ClanRankImpl implements ClanRank {

    private ClanImpl clan;
    private final int index;
    private String prefix;
    private final RankPossibilities possibilities;

    ClanRankImpl(int index, String prefix, RankPossibilities possibilities) {
        this.index = index;
        this.prefix = prefix;
        this.possibilities = possibilities;
    }

    void setClan(ClanImpl clan) {
        this.clan = clan;
    }

    @Override
    public Clan getClan() {
        return clan;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public void setPrefix(String prefix, ClanMember member) {
        this.prefix = ColorUtils.color(prefix);
    }

    @Override
    public boolean hasPossibility(RankPossibility possibility) {
        return possibilities.hasPossibility(possibility);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClanRankImpl clanRank = (ClanRankImpl) o;
        return new EqualsBuilder()
                .append(index, clanRank.index)
                .append(clan, clanRank.clan)
                .append(prefix, clanRank.prefix)
                .append(possibilities, clanRank.possibilities)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(clan)
                .append(index)
                .append(prefix)
                .append(possibilities)
                .toHashCode();
    }
}
