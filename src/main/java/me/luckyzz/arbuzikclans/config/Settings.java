package me.luckyzz.arbuzikclans.config;

import me.luckkyyz.luckapi.config.Path;

public enum Settings implements Path {

    CLAN_NAME_COLORS("clanNameColors"),
    CLAN_CREATE_MONEY("clanCreateMoney"),
    REGION_SIZE("region.size"),
    CLAN_INVITE_TIME("clanInviteTime"),
    MAX_MEMBERS_DEFAULT("maxMembersDefault");

    private final String path;

    Settings(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }
}
