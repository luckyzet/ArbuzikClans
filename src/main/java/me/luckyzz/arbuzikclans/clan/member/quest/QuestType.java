package me.luckyzz.arbuzikclans.clan.member.quest;

import me.luckkyyz.luckapi.util.Enums;

public enum QuestType {

    KILL,
    BREAK_BLOCKS;

    public static QuestType fromString(String string) {
        return Enums.valueOf(values(), string);
    }

}
