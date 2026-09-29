package net.kronhud.port.modules;

import net.kronhud.port.core.TextListModule;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.List;
import java.util.Locale;

public class ReachModule extends TextListModule {
    @Override public String id() { return "reach"; }
    @Override public String displayName() { return "Reach Distance"; }
    @Override public List<String> lines() {
        if (client.player == null || client.crosshairTarget == null
                || client.crosshairTarget.getType() == HitResult.Type.MISS) {
            return List.of("Reach: --");
        }
        double dist = client.player.getEyePos().distanceTo(client.crosshairTarget.getPos());
        String kind = client.crosshairTarget instanceof EntityHitResult ? "entity"
                : client.crosshairTarget instanceof BlockHitResult ? "block" : "?";
        return List.of(String.format(Locale.ROOT, "Reach: %.2f (%s)", dist, kind));
    }
}
