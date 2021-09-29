package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.invite.ClanInvite;
import me.luckyzz.arbuzikclans.config.Messages;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.entity.Player;

class ClanInviteImpl implements ClanInvite {

    private final ClanInviteServiceImpl clanInviteService;
    private final MessageConfig<Messages> messageConfig;

    private final String name;
    private final ClanMember member;
    private int time;

    ClanInviteImpl(ClanInviteServiceImpl clanInviteService, MessageConfig<Messages> messageConfig, Player player, ClanMember member, int time) {
        this.clanInviteService = clanInviteService;
        this.messageConfig = messageConfig;
        this.name = player.getName();
        this.member = member;
        this.time = time;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ClanMember getMember() {
        return member;
    }

    @Override
    public boolean subtractTime() {
        return time-- >= 0;
    }

    @Override
    public void acceptInvite() {
        clanInviteService.removeInvite(this);

        if (getClan().getMembers().getMember(member.getName()) == null) {
            apply(player -> messageConfig.getMessage(Messages.CLAN_INVITE_ACCEPT_CANNOT_LEFT).send(player));
            return;
        }
        Player player = getPlayer();
        if (player == null) {
            apply(player1 -> messageConfig.getMessage(Messages.SOMETHING_WENT_WRONG).send(player1));
            return;
        }
        getClan().getMembers().addMember(getPlayer(), member);
    }

    @Override
    public void denyInvite() {
        clanInviteService.removeInvite(this);

        apply(player -> messageConfig.getMessage(Messages.CLAN_INVITE_DENY_SUCCESS_EXECUTOR).send(player));
        member.apply(player -> messageConfig.getAdaptiveMessage(Messages.CLAN_INVITE_DENY_SUCCESS_TARGET)
                .placeholder("%name%", getName())
                .send(player));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClanInviteImpl that = (ClanInviteImpl) o;
        return new EqualsBuilder()
                .append(time, that.time)
                .append(clanInviteService, that.clanInviteService)
                .append(messageConfig, that.messageConfig)
                .append(name, that.name)
                .append(member, that.member)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(clanInviteService)
                .append(messageConfig)
                .append(name)
                .append(member)
                .append(time)
                .toHashCode();
    }
}
