package me.luckyzz.arbuzikclans;

import me.luckkyyz.luckapi.LuckApi;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.config.SettingConfig;
import me.luckkyyz.luckapi.database.HikariDatabase;
import me.luckkyyz.luckapi.database.HikariQueryExecutors;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckkyyz.luckapi.database.serialize.DatabaseSerializers;
import me.luckkyyz.luckapi.menu.LuckMenuService;
import me.luckkyyz.luckapi.menu.MenuService;
import me.luckkyyz.luckapi.provider.economy.EconomyProvider;
import me.luckyzz.arbuzikclans.clan.ClanService;
import me.luckyzz.arbuzikclans.clan.ClanServiceImpl;
import me.luckyzz.arbuzikclans.config.Messages;
import me.luckyzz.arbuzikclans.name.BelowNameService;
import me.luckyzz.arbuzikclans.name.ArmorStandNameService;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.dependency.SoftDependency;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

@Plugin(name = "ArbuzikClans", version = "0.1.0-SNAPSHOT")
@Author("luckyzz")
@ApiVersion(ApiVersion.Target.v1_16)
@Commands(
        @Command(name = "clan", aliases = {"clans", "c"})
)
@SoftDependency("WorldEdit")
@SoftDependency("WorldGuard")
@SoftDependency("Vault")
@SoftDependency("ProtocolLib")
public final class ArbuzikClansPlugin extends JavaPlugin {

    private LuckApi luckApi;

    private HikariDatabase clanDatabase;

    private ClanService clanService;

    @Override
    public void onEnable() {
        luckApi = LuckApi.bootstrapWith(this);
        luckApi.registerService(MenuService.class, LuckMenuService::new);

        SettingConfig config = new SettingConfig(this);
        MessageConfig<Messages> messageConfig = new MessageConfig<>(this, Messages.values());
        EconomyProvider economyProvider = EconomyProvider.openProvider();

        clanDatabase = DatabaseSerializers.yaml().deserialize(config.getSection("database"));
        QueryExecutors clanExecutors = new HikariQueryExecutors(this, clanDatabase);

        clanService = new ClanServiceImpl(config, messageConfig, economyProvider, clanExecutors);

        new ClanCommand(messageConfig, clanService);
    }

    @Override
    public void onDisable() {
        if(luckApi != null) {
            luckApi.cancel();
        }
        if(clanDatabase != null) {
            clanDatabase.close();
        }
        if(clanService != null) {
            clanService.cancel();
        }
    }
}
