package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.message.Message;
import me.luckkyyz.luckapi.util.date.DateZone;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.inventory.ItemStack;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

public class ItemShopUpgradeData extends UpgradeDataImpl {

    private final ItemStack itemStack;
    private final int price;
    private final Set<DayOfWeek> dayOfWeeks;

    ItemShopUpgradeData(Message clanMessage, ItemStack itemStack, int price, Set<DayOfWeek> dayOfWeeks) {
        super(clanMessage);
        this.itemStack = itemStack;
        this.price = price;
        this.dayOfWeeks = dayOfWeeks;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getPrice() {
        return price;
    }

    public boolean isApplyNow() {
        LocalDate date = LocalDate.now(DateZone.MOSCOW.getIdentifier());
        return dayOfWeeks.contains(date.getDayOfWeek());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemShopUpgradeData that = (ItemShopUpgradeData) o;
        return new EqualsBuilder().appendSuper(super.equals(that)).append(price, that.price).append(itemStack, that.itemStack).append(dayOfWeeks, that.dayOfWeeks).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(itemStack).append(price).append(dayOfWeeks).toHashCode();
    }
}
