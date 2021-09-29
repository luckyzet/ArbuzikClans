package me.luckyzz.arbuzikclans;

import me.luckkyyz.luckapi.command.ChatCommand;
import me.luckkyyz.luckapi.command.ExecutingChecks;
import me.luckkyyz.luckapi.command.ExecutingStrategy;
import me.luckkyyz.luckapi.command.MessageExecutingActions;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.ClanService;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.config.Messages;
import me.luckyzz.arbuzikclans.util.Permissions;
import org.bukkit.entity.Player;

final class ClanCommand {

    ClanCommand(MessageConfig<Messages> messageConfig, ClanService clanService) {
        MessageExecutingActions<Messages> actions = new MessageExecutingActions<>(messageConfig);
        ChatCommand.registerCommand("clan", ExecutingStrategy.newBuilder()
                .subCommandStrategy()
                .addCheck(ExecutingChecks.player(), actions.messageSend(Messages.ONLY_PLAYER))
                .addCheck(ExecutingChecks.permission(Permissions.CLAN), actions.messageSend(Messages.NO_PERMISSION))
                .whenSubCommandAbsent(actions.messageSend(Messages.NOT_COMMAND))
                .whenArgumentAbsent(session -> /* menu */ {})
                .addSubCommand("help", ExecutingStrategy.newBuilder().commandStrategy().addAction(actions.messageSend(Messages.CLAN_HELP)))
                .addSubCommand("create", ExecutingStrategy.newBuilder()
                        .commandStrategy()
                        .addCheck(ExecutingChecks.permission(Permissions.CLAN_CREATE), actions.messageSend(Messages.NO_PERMISSION))
                        .addCheck(ExecutingChecks.argumentsLength(1, Integer.MAX_VALUE), actions.messageSend(Messages.CLAN_CREATE_USAGE))
                        .addAction((executor, arguments) -> {
                            StringBuilder name = new StringBuilder();

                            while (arguments.hasNext()) {
                                name.append(arguments.next());
                                if(arguments.hasNext()) {
                                    name.append(" ");
                                }
                            }

                            clanService.createClan(executor.getPlayer(), name.toString());
                        })
                )
        );

        ChatCommand.registerCommand("clanchat", ExecutingStrategy.newBuilder()
                .commandStrategy()
                .addCheck(ExecutingChecks.player(), actions.messageSend(Messages.ONLY_PLAYER))
                .addCheck(ExecutingChecks.permission(Permissions.CLAN_CHAT), actions.messageSend(Messages.NO_PERMISSION))
                .addCheck(session -> clanService.hasClan(session.getExecutor().getPlayer()), actions.messageSend(Messages.NOT_CLAN))
                .addCheck(ExecutingChecks.argumentsLength(1, Integer.MAX_VALUE), actions.messageSend(Messages.CLAN_CHAT_EMPTY))
                .addAction((executor, arguments) -> {
                    Player player = executor.getPlayer();
                    Clan clan = clanService.getClanPlayer(player);
                    if(clan == null) {
                        return;
                    }
                    ClanMember member = clan.getMembers().getMember(player);
                    if(member == null) {
                        return;
                    }

                    StringBuilder builder = new StringBuilder();
                    while (arguments.hasNext()) {
                        builder.append(arguments.next());
                        if(arguments.hasNext()) {
                            builder.append(" ");
                        }
                    }

                    clan.getChat().send(builder.toString(), member);
                })
        );
    }

}
