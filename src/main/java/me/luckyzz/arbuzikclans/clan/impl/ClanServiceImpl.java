package me.luckyzz.arbuzikclans.clan.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckkyyz.luckapi.provider.economy.EconomicUser;
import me.luckkyyz.luckapi.provider.economy.EconomyProvider;
import me.luckkyyz.luckapi.util.color.ColorUtils;
import me.luckkyyz.luckapi.util.date.DateFormat;
import me.luckkyyz.luckapi.util.date.DateZone;
import me.luckkyyz.luckapi.util.date.FormatDate;
import me.luckkyyz.luckapi.util.location.LocationSerializers;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.ClanService;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberDayQuestsService;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberQuest;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRank;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRankService;
import me.luckyzz.arbuzikclans.clan.member.rank.NotUsedClanRank;
import me.luckyzz.arbuzikclans.clan.member.rank.RankRole;
import me.luckyzz.arbuzikclans.clan.region.ClanRegion;
import me.luckyzz.arbuzikclans.clan.upgrade.ClanUpgrade;
import me.luckyzz.arbuzikclans.clan.upgrade.ClanUpgradeService;
import me.luckyzz.arbuzikclans.config.ClanConfig;
import me.luckyzz.arbuzikclans.config.Messages;
import me.luckyzz.arbuzikclans.config.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ClanServiceImpl implements ClanService {

    private final Plugin plugin;
    private final ClanConfig config;
    private final MessageConfig<Messages> messageConfig;
    private final EconomyProvider economyProvider;
    private final ClanRankService rankService;
    private final MemberDayQuestsService memberDayQuestsService;
    private final ClanUpgradeService upgradeService;

    private final QueryExecutors executors;
    private final Set<Clan> clans = new HashSet<>();

    public ClanServiceImpl(Plugin plugin, ClanConfig config, MessageConfig<Messages> messageConfig, EconomyProvider economyProvider, ClanRankService rankService, MemberDayQuestsService memberDayQuestsService, ClanUpgradeService upgradeService, QueryExecutors executors) {
        this.plugin = plugin;
        this.config = config;
        this.messageConfig = messageConfig;
        this.economyProvider = economyProvider;
        this.rankService = rankService;
        this.memberDayQuestsService = memberDayQuestsService;
        this.upgradeService = upgradeService;
        this.executors = executors;
        reload();

        if (true) {
            executors.sync().update("DROP TABLE clans, clanMembers, clanRanks, clanQuests;");
        }

        executors.sync().update("CREATE TABLE IF NOT EXISTS `clans` (" +
                "`id` INT NOT NULL PRIMARY KEY, " +
                "`dateCreated` VARCHAR(64) NOT NULL, " +
                "`name` VARCHAR(64) NOT NULL, " +
                "`money` INT NOT NULL, " +
                "`coins` INT NOT NULL" +
                ")");
        executors.sync().update("CREATE TABLE IF NOT EXISTS `clanMembers` (" +
                "`name` VARCHAR(64) NOT NULL PRIMARY KEY, " +
                "`clan` INT NOT NULL, " +
                "`rank` INT NOT NULL, " +
                "`questsCompleted` INT NOT NULL" +
                ")");
        executors.sync().update("CREATE TABLE IF NOT EXISTS `clanRegions` (" +
                "`clan` INT NOT NULL PRIMARY KEY, " +
                "`center` TEXT NOT NULL, " +
                "`accessBlocks` VARCHAR(6) NOT NULL, " +
                "`accessChests` VARCHAR(6) NOT NULL, " +
                "`accessBlocksWhitelist` TEXT NOT NULL, " +
                "`accessChestsWhitelist` TEXT NOT NULL" +
                ")");
        executors.sync().update("CREATE TABLE IF NOT EXISTS `clanChats` (" +
                "`clan` INT NOT NULL PRIMARY KEY, " +
                "`enabled` VARCHAR(6) NOT NULL, " +
                "`muted` TEXT NOT NULL" +
                ")");
        executors.sync().update("CREATE TABLE IF NOT EXISTS `clanRanks` (" +
                "`clan` INT NOT NULL, " +
                "`index` INT NOT NULL, " +
                "`prefix` VARCHAR(64) NOT NULL" +
                ")");
        executors.sync().update("CREATE TABLE IF NOT EXISTS `clanQuests` (" +
                "`name` VARCHAR(64) NOT NULL, " +
                "`target` VARCHAR(16) NOT NULL, " +
                "`count` INT NOT NULL, " +
                "`needCount` INT NOT NULL" +
                ")");
        executors.sync().update("CREATE TABLE IF NOT EXISTS `clanUpgrades` (" +
                "`clan` INT NOT NULL, " +
                "`id` INT NOT NULL" +
                ")");
    }

    @Override
    public void reload() {
        clans.clear();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Multimap<String, MemberQuest> memberQuests = HashMultimap.create();
            executors.sync().result("SELECT * FROM clanQuests", result -> {
                while (result.next()) {
                    String name = result.getString("name");
                    String targetString = result.getString("target");
                    int count = result.getInt("count");
                    int needCount = result.getInt("needCount");

                    if(targetString.split(" ")[0].equals("Entity")) {
                        EntityType entityType = EntityType.valueOf(targetString.split(" ")[1]);
                        memberQuests.put(name, new MemberQuestImpl(executors, messageConfig, entityType, count, needCount));
                        continue;
                    }
                    if(targetString.split(" ")[0].equals("Block")) {
                        Material material = Material.valueOf(targetString.split(" ")[1]);
                        memberQuests.put(name, new MemberQuestImpl(executors, messageConfig, material, count, needCount));
                    }
                }
            });

            executors.sync().result("SELECT * FROM clans", clanResult -> {
                while (clanResult.next()) {
                    int id = clanResult.getInt("id");
                    LocalDate date = DateFormat.dateFormatter().parse(clanResult.getString("dateCreated"), FormatDate.DATE);
                    String name = clanResult.getString("name");
                    int money = clanResult.getInt("money");
                    int coins = clanResult.getInt("coins");

                    Set<ClanUpgrade> upgradesSet = new HashSet<>();
                    executors.sync().result("SELECT * FROM clanUpgrades WHERE clan = ?", result -> {
                        while (result.next()) {
                            int index = result.getInt("id");
                            upgradesSet.add(upgradeService.getUpgrade(index));
                        }
                    }, id);
                    ClanUpgradesImpl upgrades = new ClanUpgradesImpl(messageConfig, upgradesSet);

                    Map<Integer, ClanRank> rankMap = new HashMap<>();
                    executors.sync().result("SELECT * FROM clanRanks WHERE clan = ?", result -> {
                        while (result.next()) {
                            int index = result.getInt("index");
                            String prefix = result.getString("prefix");

                            NotUsedClanRank notUsedClanRank = rankService.getRank(index);
                            if(notUsedClanRank == null) {
                                continue;
                            }

                            ClanRank rank = new ClanRankImpl(executors, messageConfig, notUsedClanRank, prefix);
                            rankMap.put(rank.getIndex(), rank);
                        }
                    }, id);
                    ClanRanksImpl ranks = new ClanRanksImpl(executors, rankMap);

                    Map<String, ClanMember> memberMap = new HashMap<>();
                    executors.sync().result("SELECT * FROM clanMembers WHERE clan = ?", result -> {
                        while (result.next()) {
                            String memberName = result.getString("name");
                            int rankIndex = result.getInt("rank");
                            int questsCompleted = result.getInt("questsCompleted");
                            ClanRank rank = ranks.getRank(rankIndex);
                            if(rank == null) {
                                continue;
                            }
                            ClanMember member = new ClanMemberImpl(plugin, executors, messageConfig, memberName, rank, memberQuests.get(memberName), questsCompleted);
                            memberMap.put(member.getName(), member);
                        }
                    }, id);
                    ClanMembersImpl members = new ClanMembersImpl(plugin, this, config, executors, messageConfig, memberDayQuestsService, memberMap);

                    AtomicReference<ClanRegionImpl> region = new AtomicReference<>();
                    executors.sync().result("SELECT * FROM clanRegions WHERE clan = ?", result -> {
                        if(result.next()) {
                            String locationString = result.getString("center");
                            Location location = locationString == null || locationString.equals("null") ? null : LocationSerializers.string().deserialize(locationString);
                            boolean accessChests = result.getBoolean("accessChests");
                            boolean accessBlocks = result.getBoolean("accessBlocks");

                            String accessChestsString = result.getString("accessChestsWhitelist");
                            String accessBlocksString = result.getString("accessBlocksWhitelist");

                            Set<ClanMember> accessChestsWhitelist = Arrays.stream(accessChestsString.split(","))
                                    .map(memberMap::get)
                                    .collect(Collectors.toSet());
                            Set<ClanMember> accessBlocksWhitelist = Arrays.stream(accessBlocksString.split(","))
                                    .map(memberMap::get)
                                    .collect(Collectors.toSet());

                            region.set(new ClanRegionImpl(config, messageConfig, executors, location, accessChests, accessBlocks, accessChestsWhitelist, accessBlocksWhitelist));
                            return;
                        }
                        region.set(new ClanRegionImpl(config, messageConfig, executors, null, false, false, new HashSet<>(), new HashSet<>()));
                    }, id);

                    AtomicReference<ClanChatImpl> chat = new AtomicReference<>();
                    executors.sync().result("SELECT * FROM clanChats WHERE clan = ?", result -> {
                        if(result.next()) {
                            boolean chatEnabled = result.getBoolean("enabled");
                            String mutedString = result.getString("muted");

                            Set<ClanMember> muted = Arrays.stream(mutedString.split(","))
                                    .map(memberMap::get)
                                    .collect(Collectors.toSet());

                            chat.set(new ClanChatImpl(messageConfig, executors, chatEnabled, muted));

                            return;
                        }
                        chat.set(new ClanChatImpl(messageConfig, executors, true, new HashSet<>()));
                    }, id);

                    ClanRegionImpl clanRegion = region.get();
                    ClanChatImpl clanChat = chat.get();
                    Clan clan = new ClanImpl(config, messageConfig, economyProvider, executors, id, date, name, members, upgrades, ranks, money, coins, clanRegion, clanChat);
                    members.setClan(clan);
                    upgrades.setClan(clan);
                    ranks.setClan(clan);
                    clanRegion.setClan(clan);
                    clanChat.setClan(clan);

                    clans.add(clan);
                }
            });
        });
    }

    @Override
    public Collection<Clan> getAllClans() {
        return clans;
    }

    @Override
    public Clan getClan(int id) {
        return clans.stream().filter(clan -> clan.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Clan getClan(String name) {
        return clans.stream().filter(clan -> clan.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public Clan getClanPlayer(String name) {
        return clans.stream().filter(clan -> clan.getMembers().hasMember(name)).findFirst().orElse(null);
    }

    private int getClanId() {
        return clans.stream()
                .sorted(Comparator.comparingInt(Clan::getId))
                .map(Clan::getId)
                .findFirst().orElse(0);
    }

    @Override
    public void createClan(Player player, String name) {
        if (!player.isOnline()) {
            messageConfig.getMessage(Messages.SOMETHING_WENT_WRONG).send(player);
            return;
        }

        if (hasClan(player)) {
            messageConfig.getMessage(Messages.ALREADY_CLAN).send(player);
            return;
        }

        if (!config.getBoolean(Settings.CLAN_NAME_COLORS) && name.contains(ColorUtils.ALTERNATIVE_CODE_STRING)) {
            messageConfig.getMessage(Messages.CLAN_CREATE_NOT_COLORS).send(player);
            return;
        }
        int needMoney = config.getInt(Settings.CLAN_CREATE_MONEY);
        if (needMoney > 0) {
            EconomicUser economicUser = economyProvider.getUser(player);
            if (!economicUser.hasBalance(needMoney)) {
                messageConfig.getMessage(Messages.NOT_ENOUGH_MONEY).send(player);
                return;
            }
            economicUser.changeBalance(-needMoney);
        }

        name = ColorUtils.color(name);
        int id = getClanId() + 1;
        LocalDate dateCreated = LocalDate.now(DateZone.MOSCOW.getIdentifier());
        int money = 0, coins = 0;

        ClanMembersImpl members = new ClanMembersImpl(plugin, this, config, executors, messageConfig, memberDayQuestsService, new HashMap<>());
        members.addMemberSilently(player, RankRole.OWNER);

        ClanUpgradesImpl upgrades = new ClanUpgradesImpl(messageConfig, new HashSet<>());

        ClanRanksImpl ranks = new ClanRanksImpl(executors, new HashMap<>());
        rankService.getRanks().forEach(rank -> ranks.addRank(rank.toUsing()));

        ClanRegionImpl region = new ClanRegionImpl(config, messageConfig, executors, null, false, false, new HashSet<>(), new HashSet<>());
        ClanChatImpl chat = new ClanChatImpl(messageConfig, executors, true, new HashSet<>());

        Clan clan = new ClanImpl(config, messageConfig, economyProvider, executors, id, dateCreated, name, members, upgrades, ranks, money, coins, region, chat);

        members.setClan(clan);
        members.getAllMembers().forEach(member -> {
            if(member instanceof ClanMemberImpl) {
                ClanMemberImpl clanMember = (ClanMemberImpl) member;
                clanMember.setClan(clan);
            }
        });

        upgrades.setClan(clan);
        ranks.setClan(clan);
        ranks.getRanks().forEach(rank -> {
            if(rank instanceof ClanRankImpl) {
                ClanRankImpl clanRank = (ClanRankImpl) rank;
                clanRank.setClan(clan);
            }
        });

        region.setClan(clan);
        chat.setClan(clan);

        clans.add(clan);
    }

    @Override
    public void cancel() {
        clans.clear();
    }
}
