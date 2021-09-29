package me.luckyzz.arbuzikclans.clan.member.rank;

import me.luckkyyz.luckapi.util.Enums;

public enum RankRole {

    OWNER,
    VICE,
    DEFAULT(false);

    private final boolean single;

    RankRole(boolean single) {
        this.single = single;
    }

    RankRole() {
        this.single = true;
    }

    public boolean isSingle() {
        return single;
    }

    public static RankRole fromString(String string) {
        return Enums.valueOf(values(), string);
    }

}
