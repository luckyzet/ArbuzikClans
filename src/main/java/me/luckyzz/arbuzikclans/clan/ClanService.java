package me.luckyzz.arbuzikclans.clan;

import me.luckkyyz.luckapi.api.Service;
import org.bukkit.entity.Player;

public interface ClanService extends Service {

    Clan getClan(int id);

    Clan getClan(String name);

    Clan getClanPlayer(String name);

    default Clan getClanPlayer(Player player) {
        return getClanPlayer(player.getName());
    }

    void createClan(Player player, String name);

}
