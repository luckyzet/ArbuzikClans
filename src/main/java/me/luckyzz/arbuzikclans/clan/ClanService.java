package me.luckyzz.arbuzikclans.clan;

import me.luckkyyz.luckapi.api.Service;
import org.bukkit.entity.Player;

public interface ClanService extends Service {

    void createClan(String name, Player owner);

}
