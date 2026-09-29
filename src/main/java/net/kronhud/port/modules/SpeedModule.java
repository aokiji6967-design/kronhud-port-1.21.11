package net.kronhud.port.modules;

import net.kronhud.port.core.TextListModule;

import java.util.List;
import java.util.Locale;

public class SpeedModule extends TextListModule {
    @Override public String id() { return "speed"; }
    @Override public String displayName() { return "Speed"; }
    @Override public List<String> lines() {
        if (client.player == null) return List.of("Speed: --");
        double vx = client.player.getVelocity().x;
        double vz = client.player.getVelocity().z;
        double vy = client.player.getVelocity().y;
        double horizontal = Math.sqrt(vx * vx + vz * vz) * 20.0; // blocks/sec
        return List.of(String.format(Locale.ROOT, "Speed: %.2f b/s", horizontal),
                String.format(Locale.ROOT, "Vert: %.2f b/s", vy * 20.0));
    }
}
