package me.luckyzz.arbuzikclans;

import me.luckkyyz.luckapi.command.*;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckyzz.arbuzikclans.clan.ClanService;
import me.luckyzz.arbuzikclans.config.Messages;
import me.luckyzz.arbuzikclans.util.Permissions;
import org.bukkit.entity.Player;

final class ClanCommand {

    ClanCommand(MessageConfig<Messages> messageConfig, ClanService clanService) {
        MessageExecutingActions<Messages> actions = new MessageExecutingActions<>(messageConfig);

        ChatCommand.registerCommand("clan", ExecutingStrategy.newBuilder()
                .subCommandStrategy()
                .addCheck(ExecutingChecks.permission(Permissions.CLAN), actions.messageSend(Messages.NO_PERMISSION))
                .addCheck(ExecutingChecks.player(), actions.messageSend(Messages.ONLY_PLAYER))
                .whenSubCommandAbsent(actions.messageSend(Messages.NOT_COMMAND))
                .whenArgumentAbsent(actions.messageSend(Messages.NOT_COMMAND)) // help
                .addSubCommand("create", ExecutingStrategy.newBuilder()
                        .commandStrategy()
                        .addCheck(ExecutingChecks.permission(Permissions.CLAN_CREATE), actions.messageSend(Messages.NO_PERMISSION))
                        .addCheck(ExecutingChecks.argumentsLength(1), actions.messageSend(Messages.CLAN_CREATE_USAGE))
                        .addAction((executor, arguments) -> {
                            Player player = executor.getPlayer();
                            String name = arguments.get(1);

                            clanService.createClan(name, player);
                        })
                )
        );
    }

}
