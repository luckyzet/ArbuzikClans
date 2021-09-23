package me.luckyzz.arbuzikclans.clan.member.quest;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public interface MemberDayQuests {

    interface Quests {

        MemberDayQuests getMemberDayQuests();

        Set<MemberDayQuest> getAllQuests();

        Set<MemberDayQuest> generateRandom(int count);

        default Set<MemberDayQuest> generateRandom() {
            return generateRandom(ThreadLocalRandom.current().nextInt(getMemberDayQuests().getMinCount(), getMemberDayQuests().getMaxCount()));
        }

    }

    QuestComplexity getComplexity();

    int getMinCount();

    int getMaxCount();

    Quests getQuests(QuestType type);

    default Set<MemberDayQuest> generateRandom(int count) {
        int size = QuestType.values().length;
        int valueRandom = ThreadLocalRandom.current().nextInt(0, size);

        return getQuests(QuestType.values()[valueRandom]).generateRandom(count);
    }

    default Set<MemberDayQuest> generateRandom() {
        return generateRandom(ThreadLocalRandom.current().nextInt(getMinCount(), getMaxCount()));
    }

}
