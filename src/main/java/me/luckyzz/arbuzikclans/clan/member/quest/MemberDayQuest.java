package me.luckyzz.arbuzikclans.clan.member.quest;

import me.luckyzz.arbuzikclans.clan.member.ClanMember;

import java.util.concurrent.ThreadLocalRandom;

public interface MemberDayQuest {

    QuestType getType();

    default boolean isTarget(Object value) {
        return getTarget().equals(value);
    }

    Object getTarget();

    int getMinCount();

    int getMaxCount();

    MemberQuest toMemberQuest(int count, ClanMember member);

    default MemberQuest toMemberQuest(ClanMember member) {
        return toMemberQuest(ThreadLocalRandom.current().nextInt(getMinCount(), getMaxCount()), member);
    }

}
