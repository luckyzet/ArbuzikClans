package me.luckyzz.arbuzikclans.clan;

import me.luckkyyz.luckapi.api.Service;
import org.bukkit.entity.Player;

public interface ClanService extends Service {

    default boolean hasClanByMember(String name) {
        return getClanByMember(name) != null;
    }

    default boolean hasClanByMember(Player player) {
        return hasClanByMember(player.getName());
    }

    Clan getClanByMember(String name);

    default Clan getClanByMember(Player player) {
        return getClanByMember(player.getName());
    }

    void createClan(String name, Player owner);

}
