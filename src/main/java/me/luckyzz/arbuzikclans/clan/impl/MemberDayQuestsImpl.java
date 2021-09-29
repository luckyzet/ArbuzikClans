package me.luckyzz.arbuzikclans.clan.impl;

import me.luckyzz.arbuzikclans.clan.member.quest.MemberDayQuests;
import me.luckyzz.arbuzikclans.clan.member.quest.QuestComplexity;
import me.luckyzz.arbuzikclans.clan.member.quest.QuestType;

import java.util.Map;

class MemberDayQuestsImpl implements MemberDayQuests {

    private final QuestComplexity complexity;
    private final int minCount, maxCount;
    private final Map<QuestType, Quests> questsMap;

    MemberDayQuestsImpl(QuestComplexity complexity, int minCount, int maxCount, Map<QuestType, Quests> questsMap) {
        this.complexity = complexity;
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.questsMap = questsMap;
    }

    @Override
    public QuestComplexity getComplexity() {
        return complexity;
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
    public Quests getQuests(QuestType type) {
        return questsMap.get(type);
    }
}
