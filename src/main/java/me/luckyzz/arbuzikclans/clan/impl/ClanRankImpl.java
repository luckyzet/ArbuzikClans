package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckkyyz.luckapi.util.color.ColorUtils;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRank;
import me.luckyzz.arbuzikclans.clan.member.rank.NotUsedClanRank;
import me.luckyzz.arbuzikclans.clan.member.rank.RankPossibility;
import me.luckyzz.arbuzikclans.config.Messages;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

class ClanRankImpl extends NotUsedClanRankImpl implements ClanRank {

    private final QueryExecutors executors;
    private final MessageConfig<Messages> messageConfig;

    private Clan clan;

    ClanRankImpl(QueryExecutors executors, MessageConfig<Messages> messageConfig, NotUsedClanRank rank, String prefix) {
        super(messageConfig, executors, rank.getIndex(), prefix, rank.getPossibilities(), rank.getRole());
        this.executors = executors;
        this.messageConfig = messageConfig;
    }

    @Override
    public ClanRank toUsing() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Clan getClan() {
        return clan;
    }

    void setClan(Clan clan) {
        this.clan = clan;
    }

    @Override
    public void changePrefix(String prefix, ClanMember member) {
        if(!member.hasPossibility(RankPossibility.RANK_RENAME)) {
            member.apply(player -> messageConfig.getMessage(Messages.NOT_ACCESS).send(player));
            return;
        }

        String old = getPrefix();
        setPrefix(ColorUtils.color(prefix));
        executors.async().update("UPDATE clanRanks SET prefix = ? WHERE clan = ? AND index = ?", this.getPrefix(), clan.getId(), getIndex());

        member.apply(player -> messageConfig.getAdaptiveMessage(Messages.CLAN_RANK_PREFIX_CHANGE_EXECUTOR)
                .placeholder("%old_prefix%", old)
                .placeholder("%prefix%", getPrefix()).send(player));

        clan.send(messageConfig.getAdaptiveMessage(Messages.CLAN_RANK_PREFIX_CHANGE_LOCAL)
                .placeholder("%rank%", member.getRank().getPrefix())
                .placeholder("%name%", member.getName())
                .placeholder("%old_prefix%", old)
                .placeholder("%prefix%", getPrefix()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClanRankImpl clanRank = (ClanRankImpl) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(executors, clanRank.executors)
                .append(messageConfig, clanRank.messageConfig)
                .append(clan, clanRank.clan)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(executors)
                .append(messageConfig)
                .append(clan)
                .toHashCode();
    }
}
