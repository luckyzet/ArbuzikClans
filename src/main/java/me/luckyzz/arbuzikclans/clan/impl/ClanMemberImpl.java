package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckkyyz.luckapi.util.date.DateZone;
import me.luckkyyz.luckapi.util.date.FormatDate;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

class ClanMemberImpl implements ClanMember {

    private final Plugin plugin;
    private final QueryExecutors executors;
    private final MessageConfig<Messages> messageConfig;
    private final String name;
    private LocalDateTime lastJoin;
    private Clan clan;
    private ClanRank rank;
    private Collection<MemberQuest> quests;
    private int questsCompleted;

    ClanMemberImpl(Plugin plugin, QueryExecutors executors, MessageConfig<Messages> messageConfig, String name, ClanRank rank, Collection<MemberQuest> quests, int questsCompleted) {
        this.plugin = plugin;
        this.executors = executors;
        this.messageConfig = messageConfig;
        this.name = name;
        lastJoin = LocalDateTime.now(DateZone.MOSCOW.getIdentifier());
        this.rank = rank;
        this.quests = quests;
        this.questsCompleted = questsCompleted;
    }

    ClanMemberImpl(Plugin plugin, QueryExecutors executors, MessageConfig<Messages> messageConfig, String name, LocalDateTime lastJoin, ClanRank rank, Collection<MemberQuest> quests, int questsCompleted) {
        this.plugin = plugin;
        this.executors = executors;
        this.messageConfig = messageConfig;
        this.name = name;
        this.lastJoin = lastJoin;
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
    public LocalDateTime getLastJoinTime() {
        return lastJoin;
    }

    @Override
    public void updateLastJoinTime() {
        lastJoin = LocalDateTime.now(DateZone.MOSCOW.getIdentifier());
        executors.async().update("UPDATE clanMembers SET lastJoin = ? WHERE name = ?", getLastJoinTime(FormatDate.DATE_TIME), name);
    }

    @Override
    public ClanRank getRank() {
        return rank;
    }

    @Override
    public void changeRank(ClanRank rank, ClanMember member) {
        if (name.equals(member.getName())) {
            apply(player -> messageConfig.getMessage(Messages.YOURSELF).send(player));
            return;
        }
        if (this.rank.getRole() == RankRole.OWNER || !member.hasPossibility(RankPossibility.RANK_GIVE)) {
            member.apply(player -> messageConfig.getMessage(Messages.CLAN_RANK_CANNOT_GIVE).send(player));
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
        changeQuestsSilently(quests);

        apply(player -> messageConfig.getMessage(Messages.CLAN_QUESTS_UPDATED).send(player));
    }

    @Override
    public void changeQuestsSilently(Collection<MemberQuest> quests) {
        this.quests = new ArrayList<>(quests);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            executors.sync().update("DELETE FROM clanQuests WHERE name = ?", name);
            quests.forEach(quest -> executors.sync().update("INSERT INTO clanQuests VALUES (?, ?, ?, ?, ?, ?, ?)", name, quest.getDisplay(),
                    quest.getType().name(), quest.getCoins(), quest.getTargetName(), quest.getCount(), quest.getNeedCount()));
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

        return new EqualsBuilder().append(name, that.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).toHashCode();
    }
}
