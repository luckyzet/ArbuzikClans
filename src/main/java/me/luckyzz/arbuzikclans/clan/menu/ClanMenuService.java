package me.luckyzz.arbuzikclans.clan.menu;

import me.luckkyyz.luckapi.api.Service;

public interface ClanMenuService extends Service {

    ClanMenu getMenu(ClanMenuType type, Object... args);

}
