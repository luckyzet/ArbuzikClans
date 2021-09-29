package me.luckyzz.arbuzikclans.clan.member.quest;

import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;

public interface MemberQuest {

    default boolean isTarget(Object value) {
        return getTarget().equals(value);
    }

    Object getTarget();

    String getTargetName();

    default Clan getClan() {
        return getMember().getClan();
    }

    default boolean isFinished() {
        return getCount() >= getNeedCount();
    }

    ClanMember getMember();

    int getNeedCount();

    int getCount();

    void changeCount(int amount);

}
