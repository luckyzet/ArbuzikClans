package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckkyyz.luckapi.message.Message;
import me.luckkyyz.luckapi.message.serialize.MessageSerializers;
import me.luckkyyz.luckapi.util.color.ColorUtils;
import me.luckkyyz.luckapi.util.itemstack.reader.ItemReaders;
import me.luckyzz.arbuzikclans.clan.upgrade.*;
import me.luckyzz.arbuzikclans.config.ClanConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.time.DayOfWeek;
import java.util.*;

public class ConfigClanUpgradeService implements ClanUpgradeService {

    private final ClanConfig config;
    private final QueryExecutors executors;

    private final Map<Integer, ClanUpgrade> upgrades = new HashMap<>();

    public ConfigClanUpgradeService(ClanConfig config, QueryExecutors executors) {
        this.config = config;
        this.executors = executors;

        reload();
    }

    @Override
    public void reload() {
        upgrades.clear();

        ConfigurationSection mainSection = config.getSection("upgrades");
        if(mainSection == null) {
            return;
        }
        mainSection.getKeys(false).forEach(indexString -> {
            int index = Integer.parseInt(indexString);
            ConfigurationSection section = mainSection.getConfigurationSection(indexString);
            if(section == null) {
                return;
            }

            ItemStack itemStack = ItemReaders.yaml().read(section.getConfigurationSection("itemMenu")).get();

            Set<UpgradeRequirement> requirements = new HashSet<>();
            ConfigurationSection requirementSection = section.getConfigurationSection("requirements");
            if(requirementSection != null) {
                requirementSection.getKeys(false).forEach(key -> {
                    if(key.equalsIgnoreCase("coins")) {
                        requirements.add(new CoinUpgradeRequirement(requirementSection.getInt(key)));
                    }
                });
            }

            ClanUpgrade upgrade = null;

            Set<UpgradeShowRequirement> showRequirements = new HashSet<>();
            showRequirements.add(new AppliedUpgradeShowRequirement());

            UpgradeType type = UpgradeType.fromString(section.getString("type"));
            if(type == UpgradeType.MEMBER_SLOTS) {
                showRequirements.add(new MemberUpgradeShowRequirement());

                int amount = section.getInt("amount");
                int level = section.getInt("level");
                Message clanMessage = MessageSerializers.yaml().deserialize(section.getRoot(), section.getCurrentPath() + ".clanMessage");

                upgrade = new ClanUpgradeImpl(executors, index, itemStack, type, new MemberUpgradeData(clanMessage, level, amount), requirements, showRequirements);
                upgrades.put(upgrade.getIndex(), upgrade);

            }
            if(type == UpgradeType.ITEM) {
                ItemStack itemStack2 = ItemReaders.yaml().read(section.getConfigurationSection("item")).get();
                Set<DayOfWeek> dayOfWeeks = new HashSet<>();

                section.getStringList("daysOfWeek").forEach(key -> dayOfWeeks.add(DayOfWeek.valueOf(key.toUpperCase())));

                int price = section.getInt("price");
                Message clanMessage = MessageSerializers.yaml().deserialize(section.getRoot(), section.getCurrentPath() + ".clanMessage");

                upgrade = new ClanUpgradeImpl(executors, index, itemStack, type, new ItemShopUpgradeData(clanMessage, itemStack2, price, dayOfWeeks), requirements, showRequirements);
            }

            if(upgrade != null) {
                upgrades.put(upgrade.getIndex(), upgrade);
            }
        });
    }

    @Override
    public void cancel() {
        upgrades.clear();
    }

    @Override
    public Collection<ClanUpgrade> getUpgrades() {
        return upgrades.values();
    }

    @Override
    public ClanUpgrade getUpgrade(int index) {
        return upgrades.get(index);
    }
}
