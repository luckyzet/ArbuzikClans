package me.luckyzz.arbuzikclans.clan.member.quest;

import me.luckyzz.arbuzikclans.clan.member.ClanMember;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public interface MemberDayQuests {

    default List<MemberDayQuest> generateRandom(int count) {
        int size = QuestType.values().length;
        int valueRandom = ThreadLocalRandom.current().nextInt(0, size);

        return getQuests(QuestType.values()[valueRandom]).generateRandom(count);
    }

    QuestComplexity getComplexity();

    int getMinCount();

    int getMaxCount();

    Quests getQuests(QuestType type);

    default List<MemberDayQuest> generateRandom() {
        return generateRandom(ThreadLocalRandom.current().nextInt(getMinCount(), getMaxCount()));
    }

    default List<MemberQuest> generateRandom(ClanMember member) {
        return generateRandom(ThreadLocalRandom.current().nextInt(getMinCount(), getMaxCount())).stream()
                .map(quest -> quest.toMemberQuest(member))
                .collect(Collectors.toList());
    }

    interface Quests {

        MemberDayQuests getMemberDayQuests();

        List<MemberDayQuest> getAllQuests();

        List<MemberDayQuest> generateRandom(int count);

        default List<MemberDayQuest> generateRandom() {
            return generateRandom(ThreadLocalRandom.current().nextInt(getMemberDayQuests().getMinCount(), getMemberDayQuests().getMaxCount()));
        }

    }

}
