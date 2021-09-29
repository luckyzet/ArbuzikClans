package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckkyyz.luckapi.util.color.ColorUtils;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRankService;
import me.luckyzz.arbuzikclans.clan.member.rank.ConstantRankPossibilities;
import me.luckyzz.arbuzikclans.clan.member.rank.NotUsedClanRank;
import me.luckyzz.arbuzikclans.clan.member.rank.RankRole;
import me.luckyzz.arbuzikclans.config.ClanConfig;
import me.luckyzz.arbuzikclans.config.Messages;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashSet;
import java.util.Set;

public class ConfigClanRankService implements ClanRankService {

    private final ClanConfig config;
    private final MessageConfig<Messages> messageConfig;
    private final QueryExecutors executors;

    private final Set<NotUsedClanRank> ranks = new HashSet<>();

    public ConfigClanRankService(ClanConfig config, MessageConfig<Messages> messageConfig, QueryExecutors executors) {
        this.config = config;
        this.messageConfig = messageConfig;
        this.executors = executors;
        reload();
    }

    @Override
    public void reload() {
        ranks.clear();

        ConfigurationSection mainSection = config.getSection("ranks");
        if (mainSection != null) {
            mainSection.getKeys(false).forEach(indexString -> {
                int index;
                try {
                    index = Integer.parseInt(indexString);
                } catch (IllegalArgumentException exception) {
                    exception.printStackTrace();
                    return;
                }
                ConfigurationSection section = mainSection.getConfigurationSection(indexString);
                if (section == null) {
                    return;
                }

                String roleString = section.getString("role", RankRole.DEFAULT.name());
                RankRole role = RankRole.DEFAULT;
                try {
                    role = RankRole.fromString(roleString);
                } catch (IllegalArgumentException exception) {
                    exception.printStackTrace();
                }

                String prefix = ColorUtils.color(section.getString("prefix", ""));
                ranks.add(new NotUsedClanRankImpl(messageConfig, executors, index, prefix, ConstantRankPossibilities.fromRole(role), role));
            });
        }
    }

    @Override
    public Set<NotUsedClanRank> getRanks() {
        return ranks;
    }

    @Override
    public NotUsedClanRank getRank(int index) {
        return ranks.stream().filter(rank -> rank.getIndex() == index).findFirst().orElse(null);
    }

    @Override
    public NotUsedClanRank getRank(RankRole role) {
        return ranks.stream().filter(rank -> rank.getRole() == role).findFirst().orElse(null);
    }

    @Override
    public void cancel() {
        ranks.clear();
    }
}
