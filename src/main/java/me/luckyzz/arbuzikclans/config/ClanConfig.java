package me.luckyzz.arbuzikclans.config;

import me.luckkyyz.luckapi.config.SettingConfig;
import me.luckkyyz.luckapi.util.itemstack.reader.ItemReaders;
import me.luckkyyz.luckapi.util.itemstack.reader.ReadItem;
import me.luckyzz.arbuzikclans.clan.member.quest.QuestComplexity;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class ClanConfig extends SettingConfig {

    private ReadItem regionItem;

    public ClanConfig(Plugin plugin) {
        super(plugin);
        reload();
    }

    @Override
    public void reload() {
        super.reload();
        regionItem = ItemReaders.yaml().read(getSection("region.item"));
    }

    public QuestComplexity getQuestComplexity(int count) {
        return Arrays.stream(QuestComplexity.values())
                .filter(complexity -> getInt("questsCompleted." + complexity.name().toLowerCase() + ".from") >= count)
                .filter(complexity -> getInt("questsCompleted." + complexity.name().toLowerCase() + ".to") <= count)
                .findFirst().orElse(QuestComplexity.LOW);
    }

    public ReadItem getRegionItem() {
        return regionItem;
    }
}
