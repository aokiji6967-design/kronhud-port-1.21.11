package net.kronhud.port.modules;

import net.kronhud.port.core.TextListModule;

import java.util.List;
import java.util.Locale;

public class CoordsModule extends TextListModule {
    @Override public String id() { return "coords"; }
    @Override public String displayName() { return "Coordinates"; }
    @Override public List<String> lines() {
        if (client.player == null) return List.of("XYZ: --");
        return List.of(String.format(Locale.ROOT, "XYZ: %.1f / %.1f / %.1f",
                client.player.getX(), client.player.getY(), client.player.getZ()));
    }
}
