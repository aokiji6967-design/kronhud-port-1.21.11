package net.kronhud.port.modules;

import net.kronhud.port.core.TextListModule;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.Hand;

import java.util.List;

public class ArrowCounterModule extends TextListModule {
    @Override public String id() { return "arrows"; }
    @Override public String displayName() { return "Arrow Counter"; }
    @Override public List<String> lines() {
        if (client.player == null) return List.of("Arrows: --");
        boolean holdingBow =
                client.player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof RangedWeaponItem
                        || client.player.getStackInHand(Hand.OFF_HAND).getItem() instanceof RangedWeaponItem;
        if (!holdingBow) return List.of("Arrows: --");
        int count = 0;
        for (int i = 0; i < client.player.getInventory().size(); i++) {
            if (client.player.getInventory().getStack(i).isOf(Items.ARROW)) {
                count += client.player.getInventory().getStack(i).getCount();
            }
        }
        return List.of("Arrows: " + count);
    }
}
