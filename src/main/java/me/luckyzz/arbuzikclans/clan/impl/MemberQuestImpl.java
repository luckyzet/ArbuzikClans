package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberQuest;
import me.luckyzz.arbuzikclans.config.Messages;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

class MemberQuestImpl implements MemberQuest {

    private final QueryExecutors executors;
    private final MessageConfig<Messages> messageConfig;
    private final Object target;
    private final int needCount;
    private ClanMember member;
    private int count;

    MemberQuestImpl(QueryExecutors executors, MessageConfig<Messages> messageConfig, Object target, int needCount, int count) {
        this.executors = executors;
        this.messageConfig = messageConfig;
        this.target = target;
        this.needCount = needCount;
        this.count = count;
    }

    MemberQuestImpl(ClanMember member, QueryExecutors executors, MessageConfig<Messages> messageConfig, Object target, int needCount, int count) {
        this.member = member;
        this.executors = executors;
        this.messageConfig = messageConfig;
        this.target = target;
        this.needCount = needCount;
        this.count = count;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public String getTargetName() {
        return target instanceof EntityType ? "Entity " + ((EntityType) target).name() :
                target instanceof Material ? "Block " + ((Material) target).name() : "null";
    }

    @Override
    public ClanMember getMember() {
        return member;
    }

    void setMember(ClanMember member) {
        this.member = member;
    }

    @Override
    public int getNeedCount() {
        return needCount;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void changeCount(int amount) {
        this.count += amount;

        executors.async().update("UPDATE quests SET count = ? WHERE name = ?", this.count, member.getName());

        if (isFinished()) {
            member.apply(player -> messageConfig.getMessage(Messages.CLAN_QUESTS_FINISHED).send(player));
            member.addQuestCompleted();
        } else {
            member.apply(player -> messageConfig.getAdaptiveMessage(Messages.CLAN_QUESTS_COUNT_CHANGE)
                    .placeholder("%count%", this.count)
                    .placeholder("%max%", needCount)
                    .send(player));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberQuestImpl that = (MemberQuestImpl) o;
        return new EqualsBuilder()
                .append(needCount, that.needCount)
                .append(count, that.count)
                .append(executors, that.executors)
                .append(member, that.member)
                .append(target, that.target)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(executors)
                .append(member)
                .append(target)
                .append(needCount)
                .append(count)
                .toHashCode();
    }
}
