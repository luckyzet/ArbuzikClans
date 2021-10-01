package me.luckyzz.arbuzikclans.clan.member.quest;

import me.luckkyyz.luckapi.api.Typable;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;

public interface MemberQuest extends Typable<QuestType> {

    default boolean isTarget(Object value) {
        return getTarget().equals(value);
    }

    Object getTarget();

    String getTargetName();

    String getDisplay();

    default Clan getClan() {
        return getMember().getClan();
    }

    default boolean isFinished() {
        return getCount() >= getNeedCount();
    }

    ClanMember getMember();

    int getCoins();

    int getNeedCount();

    int getCount();

    void changeCount(int amount);

}
