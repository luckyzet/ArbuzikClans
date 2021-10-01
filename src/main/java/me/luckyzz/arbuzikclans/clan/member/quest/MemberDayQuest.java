package me.luckyzz.arbuzikclans.clan.member.quest;

import me.luckkyyz.luckapi.api.Typable;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;

import java.util.concurrent.ThreadLocalRandom;

public interface MemberDayQuest extends Typable<QuestType> {

    default boolean isTarget(Object value) {
        return getTarget().equals(value);
    }

    Object getTarget();

    String getDisplay();

    int getMinCount();

    int getMaxCount();

    int getCoins();

    MemberQuest toMemberQuest(int count, ClanMember member);

    default MemberQuest toMemberQuest(ClanMember member) {
        return toMemberQuest(ThreadLocalRandom.current().nextInt(getMinCount(), getMaxCount()), member);
    }

}
