package me.luckyzz.arbuzikclans.clan.member.rank;

import me.luckkyyz.luckapi.util.Enums;

public enum RankRole {

    OWNER,
    VICE,
    DEFAULT;

    public static RankRole fromString(String string) {
        return Enums.valueOf(values(), string);
    }

}
