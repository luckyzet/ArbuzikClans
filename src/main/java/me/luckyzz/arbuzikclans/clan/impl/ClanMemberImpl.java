package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberQuest;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRank;
import me.luckyzz.arbuzikclans.clan.member.rank.RankPossibility;
import me.luckyzz.arbuzikclans.clan.member.rank.RankRole;
import me.luckyzz.arbuzikclans.config.Messages;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class ClanMemberImpl implements ClanMember {

    private final Plugin plugin;
    private final QueryExecutors executors;
    private final MessageConfig<Messages> messageConfig;
    private final String name;
    private Clan clan;
    private ClanRank rank;
    private List<MemberQuest> quests;
    private int questsCompleted;

    ClanMemberImpl(Plugin plugin, QueryExecutors executors, MessageConfig<Messages> messageConfig, String name, ClanRank rank, List<MemberQuest> quests, int questsCompleted) {
        this.plugin = plugin;
        this.executors = executors;
        this.messageConfig = messageConfig;
        this.name = name;
        this.rank = rank;
        this.quests = quests;
        this.questsCompleted = questsCompleted;
    }

    @Override
    public Clan getClan() {
        return clan;
    }

    void setClan(Clan clan) {
        this.clan = clan;
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
    public void changeRank(ClanRank rank, ClanMember member) {
        RankRole role = rank.getRole();
        if (!member.hasPossibility(RankPossibility.RANK_GIVE) || (role.isSingle() && !clan.getMembers().getMembers(rank.getRole()).isEmpty())) {
            apply(player -> messageConfig.getMessage(Messages.CLAN_RANK_CANNOT_GIVE).send(player));
            return;
        }

        this.rank = rank;

        executors.async().update("UPDATE clanMembers SET rank = ? WHERE name = ?", rank.getIndex(), name);
    }

    @Override
    public Collection<MemberQuest> getQuests() {
        return quests;
    }

    @Override
    public void changeQuests(Collection<MemberQuest> quests) {
        this.quests = new ArrayList<>(quests);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            executors.sync().update("DELETE FROM clanQuests WHERE name = ?", name);
            quests.forEach(quest -> executors.sync().update("INSERT INTO clanQuests VALUES (?, ?, ?, ?)", name, quest.getTargetName(), quest.getCount(), quest.getNeedCount()));
        });
    }

    @Override
    public int getQuestsCompleted() {
        return questsCompleted;
    }

    @Override
    public void changeQuestsCompleted(int amount) {
        this.questsCompleted += amount;

        executors.async().update("UPDATE clanMembers SET questsCompleted = ? WHERE name = ?", questsCompleted, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClanMemberImpl that = (ClanMemberImpl) o;
        return new EqualsBuilder()
                .append(questsCompleted, that.questsCompleted)
                .append(plugin, that.plugin)
                .append(executors, that.executors)
                .append(messageConfig, that.messageConfig)
                .append(clan, that.clan)
                .append(name, that.name)
                .append(rank, that.rank)
                .append(quests, that.quests)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(plugin)
                .append(executors)
                .append(messageConfig)
                .append(clan)
                .append(name)
                .append(rank)
                .append(quests)
                .append(questsCompleted)
                .toHashCode();
    }
}
