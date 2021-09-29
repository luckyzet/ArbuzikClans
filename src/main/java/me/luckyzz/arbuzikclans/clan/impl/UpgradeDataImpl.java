package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.message.Message;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.upgrade.UpgradeData;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class UpgradeDataImpl implements UpgradeData {

    protected Message clanMessage;

    public UpgradeDataImpl(Message clanMessage) {
        this.clanMessage = clanMessage;
    }

    @Override
    public Message getClanMessage(ClanMember member) {
        return clanMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpgradeDataImpl that = (UpgradeDataImpl) o;
        return new EqualsBuilder().append(clanMessage, that.clanMessage).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(clanMessage).toHashCode();
    }
}
