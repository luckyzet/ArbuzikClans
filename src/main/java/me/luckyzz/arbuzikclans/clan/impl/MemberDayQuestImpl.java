package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberDayQuest;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberQuest;
import me.luckyzz.arbuzikclans.clan.member.quest.QuestType;
import me.luckyzz.arbuzikclans.config.Messages;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

class MemberDayQuestImpl implements MemberDayQuest {

    private final MessageConfig<Messages> messageConfig;
    private final QueryExecutors executors;

    private final QuestType type;
    private final Object target;
    private final String display;
    private final int minCount, maxCount;

    MemberDayQuestImpl(MessageConfig<Messages> messageConfig, QueryExecutors executors, QuestType type, Object target, String display, int minCount, int maxCount) {
        this.messageConfig = messageConfig;
        this.executors = executors;
        this.type = type;
        this.target = target;
        this.display = display;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    @Override
    public QuestType getType() {
        return type;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public String getDisplay() {
        return display;
    }

    @Override
    public int getMinCount() {
        return minCount;
    }

    @Override
    public int getMaxCount() {
        return maxCount;
    }

    @Override
    public MemberQuest toMemberQuest(int count, ClanMember member) {
        return new MemberQuestImpl(member, executors, messageConfig, display, target, maxCount, count);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDayQuestImpl that = (MemberDayQuestImpl) o;
        return new EqualsBuilder()
                .append(minCount, that.minCount)
                .append(maxCount, that.maxCount)
                .append(messageConfig, that.messageConfig)
                .append(executors, that.executors)
                .append(type, that.type)
                .append(target, that.target)
                .append(display, that.display)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(messageConfig)
                .append(executors)
                .append(type)
                .append(target)
                .append(display)
                .append(minCount)
                .append(maxCount)
                .toHashCode();
    }
}
