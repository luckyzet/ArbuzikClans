package me.luckyzz.arbuzikclans.name;

import me.luckkyyz.luckapi.api.Service;
import org.bukkit.entity.Player;

public interface BelowNameService extends Service {

    void setName(Player player, String name);

    default void removeName(Player player) {
        setName(player, null);
    }

}
