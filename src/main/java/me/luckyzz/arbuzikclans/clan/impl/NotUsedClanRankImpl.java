package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRank;
import me.luckyzz.arbuzikclans.clan.member.rank.NotUsedClanRank;
import me.luckyzz.arbuzikclans.clan.member.rank.RankPossibilities;
import me.luckyzz.arbuzikclans.clan.member.rank.RankRole;
import me.luckyzz.arbuzikclans.config.Messages;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

class NotUsedClanRankImpl implements NotUsedClanRank {

    private final MessageConfig<Messages> messageConfig;
    private final QueryExecutors executors;

    private final int index;
    private final RankPossibilities possibilities;
    private final RankRole role;
    private String prefix;

    NotUsedClanRankImpl(MessageConfig<Messages> messageConfig, QueryExecutors executors, int index, String prefix, RankPossibilities possibilities, RankRole role) {
        this.messageConfig = messageConfig;
        this.executors = executors;
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
    public ClanRank toUsing() {
        return new ClanRankImpl(executors, messageConfig, this, prefix);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotUsedClanRankImpl that = (NotUsedClanRankImpl) o;
        return new EqualsBuilder().append(index, that.index).append(messageConfig, that.messageConfig).append(executors, that.executors).append(possibilities, that.possibilities).append(role, that.role).append(prefix, that.prefix).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(messageConfig).append(executors).append(index).append(possibilities).append(role).append(prefix).toHashCode();
    }
}
