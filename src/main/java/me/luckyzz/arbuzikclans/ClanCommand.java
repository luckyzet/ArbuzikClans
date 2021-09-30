package me.luckyzz.arbuzikclans;

import me.luckkyyz.luckapi.command.ChatCommand;
import me.luckkyyz.luckapi.command.ExecutingChecks;
import me.luckkyyz.luckapi.command.ExecutingStrategy;
import me.luckkyyz.luckapi.command.MessageExecutingActions;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.util.player.PlayerFilters;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.ClanService;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.invite.ClanInvite;
import me.luckyzz.arbuzikclans.clan.member.invite.ClanInviteService;
import me.luckyzz.arbuzikclans.clan.menu.ClanMenuService;
import me.luckyzz.arbuzikclans.clan.menu.ClanMenuType;
import me.luckyzz.arbuzikclans.config.Messages;
import me.luckyzz.arbuzikclans.util.Permissions;
import org.bukkit.entity.Player;

final class ClanCommand {

    ClanCommand(MessageConfig<Messages> messageConfig, ClanService clanService, ClanMenuService clanMenuService, ClanInviteService inviteService) {
        MessageExecutingActions<Messages> actions = new MessageExecutingActions<>(messageConfig);
        ChatCommand.registerCommand("clan", ExecutingStrategy.newBuilder()
                .subCommandStrategy()
                .addCheck(ExecutingChecks.player(), actions.messageSend(Messages.ONLY_PLAYER))
                .addCheck(ExecutingChecks.permission(Permissions.CLAN), actions.messageSend(Messages.NO_PERMISSION))
                .whenSubCommandAbsent(actions.messageSend(Messages.NOT_COMMAND))
                .whenArgumentAbsent((executor, ignored) -> {
                    Player player = executor.getPlayer();

                    ClanMember member = clanService.getClanMemberPlayer(player);
                    if (member == null) {
                        messageConfig.getMessage(Messages.NOT_CLAN).send(player);
                        return;
                    }

                    clanMenuService.getMenu(ClanMenuType.MAIN).openMenu(member);
                })
                .addSubCommand("help", ExecutingStrategy.newBuilder().commandStrategy().addAction(actions.messageSend(Messages.CLAN_HELP)))
                .addSubCommand("create", ExecutingStrategy.newBuilder()
                        .commandStrategy()
                        .addCheck(ExecutingChecks.permission(Permissions.CLAN_CREATE), actions.messageSend(Messages.NO_PERMISSION))
                        .addCheck(ExecutingChecks.argumentsLength(1, Integer.MAX_VALUE), actions.messageSend(Messages.CLAN_CREATE_USAGE))
                        .addAction((executor, arguments) -> {
                            StringBuilder name = new StringBuilder();

                            while (arguments.hasNext()) {
                                name.append(arguments.next());
                                if (arguments.hasNext()) {
                                    name.append(" ");
                                }
                            }

                            clanService.createClan(executor.getPlayer(), name.toString());
                        })
                )
                .addSubCommand("region", ExecutingStrategy.newBuilder()
                        .subCommandStrategy()
                        .whenSubCommandAbsent(actions.messageSend(Messages.NOT_COMMAND))
                        .whenArgumentAbsent(actions.messageSend(Messages.CLAN_REGION_USAGE))
                        .addSubCommand("blocks", ExecutingStrategy.newBuilder()
                                .commandStrategy()
                                .addCheck(ExecutingChecks.argumentsLength(1), actions.messageSend(Messages.CLAN_REGION_USAGE))
                                .addAction((executor, arguments) -> {
                                    Clan clan = clanService.getClanPlayer(executor.getPlayer());
                                    if (clan == null) {
                                        messageConfig.getMessage(Messages.NOT_CLAN).send(executor);
                                        return;
                                    }
                                    String name = arguments.get(1);
                                    if (name.equals(executor.getPlayer().getName())) {
                                        messageConfig.getMessage(Messages.YOURSELF).send(executor);
                                        return;
                                    }
                                    ClanMember member = clan.getMembers().getMember(name);
                                    if (member == null) {
                                        messageConfig.getAdaptiveMessage(Messages.NOT_IN_CLAN).placeholder("%name%", name).send(executor);
                                        return;
                                    }
                                    clan.getRegion().setAccessBlocks(!clan.getRegion().getAccessBlocksWhitelist(member), member, clan.getMembers().getMember(executor.getPlayer()));
                                })
                        ).addSubCommand("chests", ExecutingStrategy.newBuilder()
                                .commandStrategy()
                                .addCheck(ExecutingChecks.argumentsLength(1), actions.messageSend(Messages.CLAN_REGION_USAGE))
                                .addAction((executor, arguments) -> {
                                    Clan clan = clanService.getClanPlayer(executor.getPlayer());
                                    if (clan == null) {
                                        messageConfig.getMessage(Messages.NOT_CLAN).send(executor);
                                        return;
                                    }
                                    String name = arguments.get(1);
                                    if (name.equals(executor.getPlayer().getName())) {
                                        messageConfig.getMessage(Messages.YOURSELF).send(executor);
                                        return;
                                    }
                                    ClanMember member = clan.getMembers().getMember(name);
                                    if (member == null) {
                                        messageConfig.getAdaptiveMessage(Messages.NOT_IN_CLAN).placeholder("%name%", name).send(executor);
                                        return;
                                    }
                                    clan.getRegion().setAccessChest(!clan.getRegion().getAccessChestWhitelist(member), member, clan.getMembers().getMember(executor.getPlayer()));
                                })
                        )
                ).addSubCommand("invite", ExecutingStrategy.newBuilder()
                        .commandStrategy()
                        .addCheck((session) -> clanService.hasClan(session.getExecutor().getPlayer()), actions.messageSend(Messages.NOT_CLAN))
                        .addCheck(ExecutingChecks.argumentsLength(1), actions.messageSend(Messages.CLAN_INVITE_USAGE))
                        .addCheck(ExecutingChecks.argumentFormat(1, arg -> PlayerFilters.byName(arg).isPresent()), actions.messageSend(Messages.NOT_ONLINE))
                        .addAction((executor, arguments) -> {
                            Player player = executor.getPlayer();

                            Player target = PlayerFilters.byName(arguments.get(1)).orElse(null);
                            if (target == null) {
                                messageConfig.getMessage(Messages.NOT_ONLINE).send(executor);
                                return;
                            }

                            ClanMember member = clanService.getClanMemberPlayer(player);

                            inviteService.addInvite(target, member);
                        })
                ).addSubCommand("accept", ExecutingStrategy.newBuilder()
                        .commandStrategy()
                        .addAction((executor, arguments) -> {
                            Player player = executor.getPlayer();
                            ClanInvite invite = inviteService.getInvite(player);
                            if (invite == null) {
                                messageConfig.getMessage(Messages.NOT_INVITE).send(executor);
                                return;
                            }
                            invite.acceptInvite();
                        })
                ).addSubCommand("deny", ExecutingStrategy.newBuilder()
                        .commandStrategy()
                        .addAction((executor, arguments) -> {
                            Player player = executor.getPlayer();
                            ClanInvite invite = inviteService.getInvite(player);
                            if (invite == null) {
                                messageConfig.getMessage(Messages.NOT_INVITE).send(executor);
                                return;
                            }
                            invite.denyInvite();
                        })
                ).addSubCommand("rename", ExecutingStrategy.newBuilder()
                        .commandStrategy()
                        .addCheck(ExecutingChecks.argumentsLength(1, Integer.MAX_VALUE), actions.messageSend(Messages.CLAN_RENAME_USAGE))
                        .addCheck(session -> clanService.hasClan(session.getExecutor().getPlayer()), actions.messageSend(Messages.NOT_CLAN))
                        .addAction((executor, arguments) -> {
                            Player player = executor.getPlayer();
                            ClanMember member = clanService.getClanMemberPlayer(player);
                            if (member == null) {
                                messageConfig.getMessage(Messages.NOT_CLAN).send(executor);
                                return;
                            }
                            Clan clan = member.getClan();



                            StringBuilder name = new StringBuilder();
                            while (arguments.hasNext()) {
                                name.append(arguments.next()).append(" ");
                            }

                            clan.renameClan(name.toString().trim(), member);
                        })
                ).addSubCommand("chatmute", ExecutingStrategy.newBuilder()
                        .commandStrategy()

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
