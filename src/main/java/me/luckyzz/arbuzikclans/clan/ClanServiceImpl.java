package me.luckyzz.arbuzikclans.clan;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.config.SettingConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckkyyz.luckapi.provider.economy.EconomicUser;
import me.luckkyyz.luckapi.provider.economy.EconomyProvider;
import me.luckkyyz.luckapi.util.color.ColorUtils;
import me.luckkyyz.luckapi.util.date.DateUtil;
import me.luckkyyz.luckapi.util.date.DateZone;
import me.luckkyyz.luckapi.util.date.FormatDate;
import me.luckyzz.arbuzikclans.config.Messages;
import me.luckyzz.arbuzikclans.config.Settings;
import me.luckyzz.arbuzikclans.name.BelowNameService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ClanServiceImpl implements ClanService {

    private final Plugin plugin;
    private final SettingConfig config;
    private final MessageConfig<Messages> messageConfig;
    private final EconomyProvider economyProvider;
    private final BelowNameService belowNameService;

    private final QueryExecutors executors;
    private final Cache<String, Clan> cache;

    public ClanServiceImpl(Plugin plugin, SettingConfig config, MessageConfig<Messages> messageConfig, EconomyProvider economyProvider, BelowNameService belowNameService, QueryExecutors executors) {
        this.plugin = plugin;
        this.config = config;
        this.messageConfig = messageConfig;
        this.economyProvider = economyProvider;
        this.belowNameService = belowNameService;
        this.executors = executors;

        cache = CacheBuilder.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .initialCapacity(30)
                .build();

        executors.sync().update("CREATE TABLE IF NOT EXISTS `clans` (" +
                "`id` INT NOT NULL PRIMARY KEY, " +
                "`date` VARCHAR(64) NOT NULL, " +
                "`name` VARCHAR(64) NOT NULL" +
                ")");
        executors.sync().update("CREATE TABLE IF NOT EXISTS `clanMembers` (" +
                "`name` VARCHAR(64) NOT NULL PRIMARY KEY, " +
                "`clan` INT NOT NULL" +
                ")");
    }

    QueryExecutors getExecutors() {
        return executors;
    }

    MessageConfig<Messages> getMessageConfig() {
        return messageConfig;
    }

    @Override
    public CompletableFuture<Clan> getClanName(String name) {
        return CompletableFuture.supplyAsync(() -> {
            return null;
        });
    }

    @Override
    public CompletableFuture<Clan> getClanPlayer(String player) {
        return CompletableFuture.supplyAsync(() -> {
            return null;
        });
    }

    private CompletableFuture<Integer> getLastId() {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executors.async().result("SELECT id FROM clans ORDER BY id DESC LIMIT 1", result -> {
            if(!result.next()) {
                future.complete(0);
                return;
            }
            future.complete(result.getInt("id"));
        });
        return future;
    }

    @Override
    public CompletableFuture<Clan> createClan(String name, Player owner) {
        return CompletableFuture.supplyAsync(() -> {
            if(!owner.isOnline()) {
                return null;
            }

            if(!config.getBoolean(Settings.CLAN_NAME_COLORS) && name.contains(ColorUtils.ALTERNATIVE_CODE_STRING)) {
                messageConfig.getMessage(Messages.CLAN_CREATE_NOT_COLORS).send(owner);
                return null;
            }

            int id;

            try {
                id = getLastId().get(5, TimeUnit.SECONDS) + 1;
            } catch (InterruptedException | TimeoutException | ExecutionException exception) {
                exception.printStackTrace();
                messageConfig.getMessage(Messages.SOMETHING_WENT_WRONG).send(owner);
                return null;
            }

            EconomicUser economicUser = economyProvider.getUser(owner);
            int clanCreateMoney = config.getInt(Settings.CLAN_CREATE_MONEY);
            if(clanCreateMoney > 0) {
                if(!economicUser.hasBalance(clanCreateMoney)) {
                    messageConfig.getAdaptiveMessage(Messages.NOT_ENOUGH_MONEY)
                            .placeholder("%balance%", (int) economicUser.getBalance())
                            .placeholder("%need_balance%", clanCreateMoney)
                            .placeholder("%need%", clanCreateMoney - (int) economicUser.getBalance())
                            .send(owner);
                    return null;
                }
                economicUser.changeBalance(-clanCreateMoney);
            }

            Clan clan = new ClanImpl(this, id, DateUtil.getDate(DateZone.MOSCOW), name, owner);
            cache.put(clan.getName(), clan);

            executors.sync().update("INSERT INTO clans VALUES (?, ?, ?)", clan.getId(), clan.getDateCreated(FormatDate.DATE_TIME), clan.getName());
            messageConfig.getAdaptiveMessage(Messages.CLAN_CREATE_SUCCESS)
                    .placeholder("%name%", clan.getName())
                    .send(owner);

            Bukkit.getScheduler().runTask(plugin, () -> belowNameService.setName(owner, clan.getName()));

            return clan;
        });
    }

    @Override
    public void cancel() {
        cache.invalidateAll();
    }
}
