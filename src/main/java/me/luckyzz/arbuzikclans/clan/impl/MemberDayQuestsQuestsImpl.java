package me.luckyzz.arbuzikclans.clan.impl;

import me.luckyzz.arbuzikclans.clan.member.quest.MemberDayQuest;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberDayQuests;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class MemberDayQuestsQuestsImpl implements MemberDayQuests.Quests {

    private final MemberDayQuests memberDayQuests;
    private final List<MemberDayQuest> quests;

    MemberDayQuestsQuestsImpl(MemberDayQuests memberDayQuests, List<MemberDayQuest> quests) {
        this.memberDayQuests = memberDayQuests;
        this.quests = quests;
    }

    void addQuest(MemberDayQuest quest) {
        quests.add(quest);
    }

    @Override
    public MemberDayQuests getMemberDayQuests() {
        return memberDayQuests;
    }

    @Override
    public List<MemberDayQuest> getAllQuests() {
        return quests;
    }

    @Override
    public List<MemberDayQuest> generateRandom(int count) {
        List<MemberDayQuest> selected = new ArrayList<>();

        List<MemberDayQuest> quests = new ArrayList<>(this.quests);

        for (int i = 0; i < count; ) {
            MemberDayQuest quest = quests.remove(ThreadLocalRandom.current().nextInt(0, quests.size()));
            if (selected.contains(quest)) {
                continue;
            }

            selected.add(quest);
            i++;
        }

        return selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDayQuestsQuestsImpl that = (MemberDayQuestsQuestsImpl) o;
        return new EqualsBuilder()
                .append(memberDayQuests, that.memberDayQuests)
                .append(quests, that.quests)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(memberDayQuests)
                .append(quests)
                .toHashCode();
    }
}
