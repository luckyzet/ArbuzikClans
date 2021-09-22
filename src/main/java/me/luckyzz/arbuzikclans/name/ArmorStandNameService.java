package me.luckyzz.arbuzikclans.name;

import me.luckkyyz.luckapi.event.ExtendedListener;
import me.luckkyyz.luckapi.util.color.ColorUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArmorStandNameService extends ExtendedListener implements BelowNameService {

    private final Map<UUID, ArmorStand> standMap = new HashMap<>();

    public ArmorStandNameService(Plugin plugin) {
        super(plugin);
    }

    private void quit(PlayerEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ArmorStand armorStand = standMap.remove(uuid);
        if(armorStand != null) {
            armorStand.remove();
        }
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        quit(event);
    }

    @EventHandler
    private void onKick(PlayerKickEvent event) {
        quit(event);
    }

    @EventHandler
    private void onChunk(ChunkLoadEvent event) {
        Arrays.stream(event.getChunk().getEntities()).forEach(entity -> {
            if(entity.hasMetadata("belowName")) {
                entity.remove();
            }
        });
    }

    @Override
    public void setName(Player player, String name) {
        String displayName = ColorUtils.color(name);

        UUID uuid = player.getUniqueId();

        ArmorStand armorStand = standMap.get(uuid);
        if(armorStand != null) {
            armorStand.setCustomName(displayName);
            return;
        }
        armorStand = player.getWorld().spawn(player.getLocation().add(0, 1, 0), ArmorStand.class);
        armorStand.setMetadata("belowName", new FixedMetadataValue(plugin, true));
        armorStand.setInvulnerable(true);
        armorStand.setVisible(false);
        armorStand.setInvisible(true);
        //armorStand.setSmall(true);
        armorStand.setMarker(true);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(displayName);
        player.addPassenger(armorStand);
    }

    @Override
    public void reload() {
        cancel();
    }

    @Override
    public void cancel() {
        standMap.values().forEach(ArmorStand::remove);
        standMap.clear();
    }
}
