package me.luckyzz.arbuzikclans.clan.upgrade;

import me.luckkyyz.luckapi.util.Enums;

public enum UpgradeType {

    ITEM,
    MEMBER_SLOTS;

    public static UpgradeType fromString(String string) {
        return Enums.valueOf(values(), string);
    }

}
