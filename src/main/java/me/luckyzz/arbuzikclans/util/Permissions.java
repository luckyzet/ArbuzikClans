package me.luckyzz.arbuzikclans.util;

import me.luckkyyz.luckapi.util.permission.PermissionNode;

public final class Permissions {

    private Permissions() {
        throw new UnsupportedOperationException();
    }

    public static final PermissionNode CLAN = PermissionNode.node("arbuzikclans.clan");

    public static final PermissionNode CLAN_CREATE = PermissionNode.node("arbuzikclans.clan.create");

}
