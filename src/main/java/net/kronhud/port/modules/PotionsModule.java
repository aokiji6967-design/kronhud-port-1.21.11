package net.kronhud.port.modules;

import net.kronhud.port.core.TextListModule;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.ArrayList;
import java.util.List;

public class PotionsModule extends TextListModule {
    @Override public String id() { return "potions"; }
    @Override public String displayName() { return "Active Effects"; }
    @Override public List<String> lines() {
        List<String> out = new ArrayList<>();
        if (client.player == null || client.player.getStatusEffects().isEmpty()) {
            out.add("Effects: none");
            return out;
        }
        for (StatusEffectInstance inst : client.player.getStatusEffects()) {
            int totalSeconds = inst.getDuration() / 20;
            int m = totalSeconds / 60;
            int s = totalSeconds % 60;
            String name = inst.getEffectType().value().getName().getString();
            int amp = inst.getAmplifier() + 1;
            out.add(String.format("%s %d - %d:%02d", name, amp, m, s));
        }
        return out;
    }
}
