package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.message.Message;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class MemberUpgradeData extends UpgradeDataImpl {

    private final int level;
    private final int amount;

    MemberUpgradeData(Message clanMessage, int level, int amount) {
        super(clanMessage);
        this.level = level;
        this.amount = amount;
    }

    @Override
    public Message getClanMessage(ClanMember member) {
        return super.getClanMessage(member).toAdaptive()
                ;
    }

    public int getLevel() {
        return level;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberUpgradeData that = (MemberUpgradeData) o;
        return new EqualsBuilder().appendSuper(super.equals(that)).append(level, that.level).append(amount, that.amount).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(level).append(amount).toHashCode();
    }
}
