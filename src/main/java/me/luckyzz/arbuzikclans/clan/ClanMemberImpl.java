package me.luckyzz.arbuzikclans.clan;

import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.rank.ClanRank;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.entity.Player;

class ClanMemberImpl implements ClanMember {

    private ClanImpl clan;
    private final String name;
    private ClanRank rank;

    ClanMemberImpl(ClanImpl clan, String name) {
        this.clan = clan;
        this.name = name;
    }

    ClanMemberImpl(ClanImpl clan, Player player) {
        this(clan, player.getName());
    }

    ClanMemberImpl(String name) {
        this.name = name;
    }

    void setClan(ClanImpl clan) {
        this.clan = clan;
    }

    @Override
    public Clan getClan() {
        return clan;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ClanRank getRank() {
        return rank;
    }

    @Override
    public void setRank(ClanRank rank, ClanMember member) {
        this.rank = rank;
    }

    @Override
    public void sendChat(String message) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClanMemberImpl that = (ClanMemberImpl) o;
        return new EqualsBuilder()
                .append(clan, that.clan)
                .append(name, that.name)
                .append(rank, that.rank)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(clan)
                .append(name)
                .append(rank)
                .toHashCode();
    }
}
