package me.luckyzz.arbuzikclans.clan.member.invite;

import me.luckkyyz.luckapi.api.Service;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import org.bukkit.entity.Player;

public interface ClanInviteService extends Service {

    void addInvite(Player player, ClanMember member);

    ClanInvite getInvite(String name);

    default ClanInvite getInvite(Player player) {
        return getInvite(player.getName());
    }

}
