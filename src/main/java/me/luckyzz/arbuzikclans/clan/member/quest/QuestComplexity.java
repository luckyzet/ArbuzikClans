package me.luckyzz.arbuzikclans.clan.member.quest;

import me.luckkyyz.luckapi.util.Enums;

public enum QuestComplexity {

    LOW,
    NORMAL,
    HIGH;

    public static QuestComplexity fromString(String string) {
        return Enums.valueOf(values(), string);
    }

}
