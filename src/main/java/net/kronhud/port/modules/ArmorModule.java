package net.kronhud.port.modules;

import net.kronhud.port.core.TextListModule;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ArmorModule extends TextListModule {
    private static final String[] NAMES = {"Boots", "Legs", "Chest", "Head"};

    @Override public String id() { return "armor"; }
    @Override public String displayName() { return "Armor Durability"; }
    @Override public List<String> lines() {
        List<String> out = new ArrayList<>();
        if (client.player == null) {
            out.add("Armor: --");
            return out;
        }
        List<ItemStack> armor = new ArrayList<>();
        client.player.getInventory().getArmorInventory().forEach(armor::add);
        boolean any = false;
        for (int i = armor.size() - 1; i >= 0; i--) {
            ItemStack stack = armor.get(i);
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
