package me.luckyzz.arbuzikclans.clan.menu;

import me.luckkyyz.luckapi.api.Typable;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;

public interface ClanMenu extends Typable<ClanMenuType> {

    void openMenu(ClanMember member, Object... args);

}
