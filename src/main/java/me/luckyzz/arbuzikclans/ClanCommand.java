package me.luckyzz.arbuzikclans;

import me.luckkyyz.luckapi.api.Handle;
import me.luckkyyz.luckapi.command.*;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.menu.PreparedMenu;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.ClanMember;
import me.luckyzz.arbuzikclans.clan.ClanService;
import me.luckyzz.arbuzikclans.config.Messages;
import me.luckyzz.arbuzikclans.util.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

final class ClanCommand {

    ClanCommand(MessageConfig<Messages> messageConfig, ClanService clanService) {
        MessageExecutingActions<Messages> actions = new MessageExecutingActions<>(messageConfig);

        ChatCommand.registerCommand("clan", ExecutingStrategy.newBuilder()
                .subCommandStrategy()
                .addCheck(ExecutingChecks.player(), actions.messageSend(Messages.ONLY_PLAYER))
                .addCheck(ExecutingChecks.permission(Permissions.CLAN), actions.messageSend(Messages.NO_PERMISSION))
                .whenSubCommandAbsent(actions.messageSend(Messages.NOT_COMMAND))
                .whenArgumentAbsent((executor, arguments) -> {
                    Player player = executor.getPlayer();
                    Clan clan = clanService.getClanByMember(player);
                    if(clan == null) {
                        executor.send(messageConfig.getMessage(Messages.NOT_CLAN));
                        return;
                    }
                    ClanMember member = clan.getMembers().getMember(player);

                })
                .addSubCommand("create", ExecutingStrategy.newBuilder()
                        .commandStrategy()
                        .addCheck(ExecutingChecks.permission(Permissions.CLAN_CREATE), actions.messageSend(Messages.NO_PERMISSION))
                        .addCheck(ExecutingChecks.argumentsLength(1), actions.messageSend(Messages.CLAN_CREATE_USAGE))
                        .addAction((executor, arguments) -> {
                            Player player = executor.getPlayer();
                            String name = arguments.get(1);

                            clanService.createClan(name, player);
                        }))
                .addSubCommand("help", ExecutingStrategy.newBuilder().commandStrategy().addAction(actions.messageSend(Messages.CLAN_HELP)))
        );

        ChatCommand.registerCommand("clanchat", ExecutingStrategy.newBuilder()
                .commandStrategy()
                .addCheck(ExecutingChecks.player(), actions.messageSend(Messages.ONLY_PLAYER))
                .addCheck(ExecutingChecks.permission(Permissions.CLAN_CHAT), actions.messageSend(Messages.NO_PERMISSION))
                .addCheck(session -> clanService.hasClanByMember(session.getExecutor().getPlayer()), actions.messageSend(Messages.NOT_CLAN))
                .addCheck(ExecutingChecks.argumentsLength(1, Integer.MAX_VALUE), actions.messageSend(Messages.CLAN_CHAT_EMPTY))
                .addAction((executor, arguments) -> {
                    Player player = executor.getPlayer();
                    Clan clan = clanService.getClanByMember(player);
                    if(clan == null) {
                        return;
                    }
                    ClanMember member = clan.getMembers().getMember(player);

                    StringBuilder builder = new StringBuilder();
                    while (arguments.hasNext()) {
                        builder.append(arguments.next()).append(" ");
                    }

                    member.sendChat(builder.toString());
                })
        );
    }

}
