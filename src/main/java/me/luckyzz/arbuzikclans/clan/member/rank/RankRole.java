package me.luckyzz.arbuzikclans.clan.member.rank;

import me.luckkyyz.luckapi.util.Enums;

public enum RankRole {

    OWNER(true, 3),
    VICE(true, 2),
    DEFAULT(false, 1);

    private final boolean single;
    private final int priority;

    RankRole(boolean single, int priority) {
        this.single = single;
        this.priority = priority;
    }

    public boolean isHigher(RankRole role) {
        return priority >= role.priority;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isSingle() {
        return single;
    }

    public static RankRole fromString(String string) {
        return Enums.valueOf(values(), string);
    }

}
