package me.luckyzz.arbuzikclans.clan.impl;

import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberDayQuest;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberQuest;
import me.luckyzz.arbuzikclans.clan.member.quest.QuestType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

class MemberDayQuestImpl implements MemberDayQuest {

    private final QuestType type;
    private final Object target;
    private final int minCount, maxCount;

    MemberDayQuestImpl(QuestType type, Object target, int minCount, int maxCount) {
        this.type = type;
        this.target = target;
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
    public int getMinCount() {
        return minCount;
    }

    @Override
    public int getMaxCount() {
        return maxCount;
    }

    @Override
    public MemberQuest toMemberQuest(int count, ClanMember member) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDayQuestImpl that = (MemberDayQuestImpl) o;
        return new EqualsBuilder()
                .append(minCount, that.minCount)
                .append(maxCount, that.maxCount)
                .append(type, that.type)
                .append(target, that.target)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(type)
                .append(target)
                .append(minCount)
                .append(maxCount)
                .toHashCode();
    }
}
