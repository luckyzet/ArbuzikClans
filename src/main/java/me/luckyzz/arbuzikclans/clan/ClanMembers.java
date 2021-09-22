package me.luckyzz.arbuzikclans.clan;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface ClanMembers {

    Clan getClan();

    ClanMember getMember(String name);

    default ClanMember getMember(Player player) {
        return getMember(player.getName());
    }

    Collection<ClanMember> getAllMembers();

    void addOwnerMember(Player player);

}
