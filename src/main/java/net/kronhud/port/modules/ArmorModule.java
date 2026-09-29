package net.kronhud.port.modules;

import net.kronhud.port.core.TextListModule;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ArmorModule extends TextListModule {
    private static final EquipmentSlot[] SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };
    private static final String[] NAMES = {"Head", "Chest", "Legs", "Boots"};

    @Override public String id() { return "armor"; }
    @Override public String displayName() { return "Armor Durability"; }
    @Override public List<String> lines() {
        List<String> out = new ArrayList<>();
        if (client.player == null) {
            out.add("Armor: --");
            return out;
        }
        boolean any = false;
        for (int i = 0; i < SLOTS.length; i++) {
            ItemStack stack = client.player.getEquippedStack(SLOTS[i]);
            if (stack.isEmpty()) continue;
            any = true;
            int max = stack.getMaxDamage();
            int dmg = stack.getDamage();
            int pct = max > 0 ? Math.round((1f - (float) dmg / max) * 100f) : 100;
            out.add(NAMES[i] + ": " + pct + "%");
        }
        if (!any) out.add("Armor: none");
        return out;
    }
}
