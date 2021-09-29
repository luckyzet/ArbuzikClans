package me.luckyzz.arbuzikclans.clan.member.quest;

import me.luckkyyz.luckapi.api.Service;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;

public interface MemberDayQuestsService extends Service {

    MemberDayQuests getQuests(QuestComplexity complexity);

    MemberDayQuests getQuests(ClanMember member);

}
