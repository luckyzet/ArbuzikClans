package me.luckyzz.arbuzikclans.clan.member.quest;

import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;

public interface MemberQuest extends MemberDayQuest {

    @Override
    default MemberQuest toMemberQuest(ClanMember member) {
        throw new UnsupportedOperationException();
    }

    @Override
    default MemberQuest toMemberQuest(int count, ClanMember member) {
        throw new UnsupportedOperationException();
    }

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
